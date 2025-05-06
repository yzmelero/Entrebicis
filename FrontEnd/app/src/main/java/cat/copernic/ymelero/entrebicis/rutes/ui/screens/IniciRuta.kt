package cat.copernic.ymelero.entrebicis.rutes.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.parametres.data.ParamRepository
import cat.copernic.ymelero.entrebicis.parametres.domain.ParamUseCases
import cat.copernic.ymelero.entrebicis.parametres.ui.viewmodel.ParamViewModel
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun IniciRutaScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val paramViewModel = remember { ParamViewModel(ParamUseCases(ParamRepository())) }

    // Paràmetre del sistema
    val tempsAturadaMinuts by paramViewModel.tempsMaximAturada.collectAsState()
    val tempsAturadaMilisegons = tempsAturadaMinuts * 60 * 1000
    LaunchedEffect(Unit) { paramViewModel.carregarTempsMaximAturada() }

    val gestorUbicacio = remember { LocationServices.getFusedLocationProviderClient(context) }
    val estatCamera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.3851, 2.1734), 14f)
    }

    val puntsRuta = remember { mutableStateListOf<LatLng>() }
    val ultimMoviment = remember { mutableStateOf(System.currentTimeMillis()) }
    var permisUbicacioDonat by remember { mutableStateOf(false) }
    var rutaEnMarxa by rememberSaveable { mutableStateOf(false) }

    DemanarPermisUbicacio(context) {
        permisUbicacioDonat = true
    }

    fun iniciarRuta() {
        val peticioUbicacio = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            gestorUbicacio.requestLocationUpdates(
                peticioUbicacio,
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation?.let { ubicacio ->
                            val punt = LatLng(ubicacio.latitude, ubicacio.longitude)
                            puntsRuta.add(punt)
                            ultimMoviment.value = System.currentTimeMillis()
                        }
                    }
                },
                null
            )
        }
    }

    // Finalització per inactivitat
    LaunchedEffect(rutaEnMarxa) {
        if (rutaEnMarxa) {
            while (true) {
                val tempsParat = System.currentTimeMillis() - ultimMoviment.value
                if (tempsParat > tempsAturadaMilisegons) {
                    rutaEnMarxa = false
                    Toast.makeText(context, "Ruta finalitzada per inactivitat", Toast.LENGTH_LONG)
                        .show()
                    break
                }
                delay(10_000)
            }
        }
    }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB3F0F8))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(navController, userViewModel)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ruta",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Mapa més alt
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(0.9f)
                    .padding(8.dp)
            ) {
                if (permisUbicacioDonat) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = estatCamera,
                        properties = MapProperties(isMyLocationEnabled = true),
                        uiSettings = MapUiSettings(zoomControlsEnabled = false)
                    ) {
                        if (puntsRuta.size >= 2) {
                            Polyline(points = puntsRuta)
                        }
                    }
                } else {
                    Text(
                        text = "Permís de localització no concedit",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botó iniciar/finalitzar dinàmic amb icona
            Button(
                onClick = {
                    rutaEnMarxa = !rutaEnMarxa
                    if (rutaEnMarxa) {
                        iniciarRuta()
                        Toast.makeText(context, "Ruta iniciada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ruta finalitzada", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rutaEnMarxa) Color(0xFFF44336) else Color(0xFF4CAF50)
                ),
                modifier = Modifier
                    .size(width = 160.dp, height = 60.dp)
            ) {
                Icon(
                    imageVector = if (rutaEnMarxa) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (rutaEnMarxa) "Finalitzar" else "Iniciar",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
        BottomSection(navController, userViewModel, 2)
    }
}

@Composable
fun DemanarPermisUbicacio(context: Context, onPermisConcedit: () -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedit ->
        if (concedit) onPermisConcedit()
        else Toast.makeText(context, "Permís rebutjat", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            onPermisConcedit()
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

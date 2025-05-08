package cat.copernic.ymelero.entrebicis.rutes.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.rutes.data.RutaRepository
import cat.copernic.ymelero.entrebicis.rutes.domain.RutaUseCases
import cat.copernic.ymelero.entrebicis.rutes.ui.viewmodel.RutaViewModel
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun IniciRutaScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val rutaViewModel = remember { RutaViewModel(RutaUseCases(RutaRepository())) }
    val usuari by userViewModel.currentUser.collectAsState()
    val ruta by rutaViewModel.rutaActual.collectAsState()
    var ubicacioAutoritzada by remember { mutableStateOf(false) }
    val clientUbicacio = remember { LocationServices.getFusedLocationProviderClient(context) }
    val estatCamera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.3851, 2.1734), 14f)
    }

    DemanarPermisUbicacio(context) { ubicacioAutoritzada = true }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB3F0F8))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        header(navController, userViewModel)
        Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB3F0F8))
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Iniciar Ruta",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(540.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    if (ubicacioAutoritzada) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = estatCamera,
                            properties = MapProperties(isMyLocationEnabled = true),
                            uiSettings = MapUiSettings(zoomControlsEnabled = true)
                        )  {
                            Polyline(
                                points = rutaViewModel.puntsRuta,
                                color = Color.Blue
                            )
                        }
                    } else {
                        Text(
                            text = "Cal permís per mostrar el mapa",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                if (ruta == null) {
                    Button(
                        onClick = {
                            rutaViewModel.iniciarRuta(usuari!!)
                        },
                        modifier = Modifier
                            .height(60.dp)
                            .width(180.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Iniciar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Iniciar Ruta", fontSize = 18.sp)
                    }
                } else {
                    Button(
                        onClick = {
                            rutaViewModel.finalitzarRuta()
                            Toast.makeText(context, "Ruta finalitzada!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .height(60.dp)
                            .width(180.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = "Finalitzar")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Finalitzar Ruta", fontSize = 18.sp)
                    }
                }
            }
        }
        BottomSection(navController, userViewModel, 0)
    }
    LaunchedEffect(ruta) {
        if (ruta != null) {
            Toast.makeText(context, "Ruta iniciada correctament!", Toast.LENGTH_SHORT).show()

            while (true) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                    clientUbicacio.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            rutaViewModel.afegirPuntGPS(
                                lat = location.latitude,
                                lng = location.longitude
                            )
                        }
                    }
                } else {
                    Log.w("IniciRutaScreen", "Permís d'ubicació no concedit en aquest moment")
                }
                delay(5000)
            }
        }
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

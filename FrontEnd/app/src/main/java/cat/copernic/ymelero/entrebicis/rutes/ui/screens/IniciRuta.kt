package cat.copernic.ymelero.entrebicis.rutes.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@Composable
fun IniciRutaScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val rutaViewModel = remember { RutaViewModel(RutaUseCases(RutaRepository())) }
    val rutaFinalitzada by rutaViewModel.rutaFinalitzada.collectAsState()
    val errorRuta by rutaViewModel.errorRuta.collectAsState()
    val usuari by userViewModel.currentUser.collectAsState()
    val parametres by userViewModel.parametresSistema.collectAsState()
    val ruta by rutaViewModel.rutaActual.collectAsState()
    var ubicacioAutoritzada by remember { mutableStateOf(false) }
    val clientUbicacio = remember { LocationServices.getFusedLocationProviderClient(context) }
    val estatCamera = rememberCameraPositionState()

    DisposableEffect(Unit) {
        onDispose {
            if (ruta != null) {
                rutaViewModel.finalitzarRuta()
                Log.i("IniciRutaScreen", "Ruta finalitzada per onDispose")
            }
        }
    }

    DemanarPermisUbicacio(context) { ubicacioAutoritzada = true }
    LaunchedEffect(Unit) {
        userViewModel.carregarParametresSistema()
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val location = clientUbicacio.lastLocation.await()
            location?.let {
                val inicial = LatLng(it.latitude, it.longitude)
                estatCamera.move(CameraUpdateFactory.newLatLngZoom(inicial, 17f))
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD3FCFF))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        header(navController, userViewModel)
        Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(500.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                if (ubicacioAutoritzada) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize()
                                .background(Color.White)
                                .padding(10.dp),
                            cameraPositionState = estatCamera,
                            properties = MapProperties(isMyLocationEnabled = true),
                            uiSettings = MapUiSettings(zoomControlsEnabled = true)
                        )  {
                            Polyline(
                                points = rutaViewModel.puntsRuta.toList(),
                                color = Color.Blue,
                                width = 24f
                            )
                        }
                    } else {
                        Text(
                            text = "Cal permís per mostrar el mapa",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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
        BottomSection(navController, userViewModel, 2)
    }

    LaunchedEffect(ruta) {
        if (ruta != null) {
            Toast.makeText(context, "Ruta iniciada correctament!", Toast.LENGTH_SHORT).show()

            val segonsEntrePunts = 2
            val margeConsideratAturat = 5f
            val tempsMaximAturat = parametres!!.tempsMaximAturada * 60
            var segonsAturat = 0
            var anterior: LatLng? = null
            var finalitzar = false

            while (!finalitzar) {
                try {
                    val location = clientUbicacio.lastLocation.await()
                    if (location != null) {
                        val actual = LatLng(location.latitude, location.longitude)

                        if (anterior != null) {
                            val resultat = FloatArray(1)
                            Location.distanceBetween(
                                anterior.latitude, anterior.longitude,
                                actual.latitude, actual.longitude,
                                resultat
                            )
                            val distanciaEntrePunts = resultat[0]

                            if (distanciaEntrePunts < margeConsideratAturat) {
                                segonsAturat += segonsEntrePunts
                            } else {
                                segonsAturat = 0
                            }

                            if (segonsAturat >= tempsMaximAturat) {
                                rutaViewModel.finalitzarRuta()
                                Toast.makeText(context, "Temps d'aturada superat. Ruta finalitzada automàticament.", Toast.LENGTH_SHORT).show()
                                finalitzar = true
                            }
                        }
                        rutaViewModel.afegirPuntGPS(actual.latitude, actual.longitude)
                        anterior = actual
                        estatCamera.animate(CameraUpdateFactory.newLatLngZoom(actual, 20f))
                    }
                } catch (e: SecurityException) {
                    Log.e("IniciRutaScreen", "Permís denegat: ${e.message}")
                    finalitzar = true
                } catch (e: Exception) {
                    Log.e("IniciRutaScreen", "Error inesperat: ${e.message}")
                }
                delay(segonsEntrePunts * 1000L)
            }
        }
    }

    LaunchedEffect(rutaFinalitzada) {
        rutaFinalitzada?.let { ruta ->
            navController.navigate("detallsRuta/${ruta.id}")
            rutaViewModel.resetRutaFinalitzada()
        }
    }
    LaunchedEffect(errorRuta) {
        errorRuta?.let { msg ->
            Toast.makeText(context, "Error: $msg", Toast.LENGTH_LONG).show()
            rutaViewModel.resetRutaFinalitzada()
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
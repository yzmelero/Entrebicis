package cat.copernic.ymelero.entrebicis.rutes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.rutes.data.RutaRepository
import cat.copernic.ymelero.entrebicis.rutes.domain.RutaUseCases
import cat.copernic.ymelero.entrebicis.rutes.ui.viewmodel.RutaViewModel
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun DetallsRutaScreen(navController: NavController, userViewModel: UserViewModel) {
    val rutaViewModel = remember { RutaViewModel(RutaUseCases(RutaRepository())) }
    val rutaCarregada by rutaViewModel.rutaCarregada.collectAsState()

    val idRuta = remember(navController) {
        navController.currentBackStackEntry?.arguments?.getString("idRuta")?.toLongOrNull()
    }

    LaunchedEffect(idRuta) {
        if (idRuta != null && rutaCarregada == null) {
            rutaViewModel.carregarRutaPerId(idRuta)
            userViewModel.carregarParametresSistema()
        }
    }

    val parametres by userViewModel.parametresSistema.collectAsState()
    val cameraState = rememberCameraPositionState()
    val puntsRuta = remember { mutableStateListOf<LatLng>() }
    val ruta = rutaCarregada

    LaunchedEffect(ruta) {
        userViewModel.carregarParametresSistema()
        ruta?.let {
            puntsRuta.clear()
            ruta.puntGPS?.sortedBy { it.marcaTemps }?.forEach {
                puntsRuta.add(LatLng(it.latitud, it.longitud))
            }

            if (puntsRuta.isNotEmpty()) {
                cameraState.move(CameraUpdateFactory.newLatLngZoom(puntsRuta.first(), 16f))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD3FCFF))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(navController, userViewModel)
            Spacer(modifier = Modifier.height(10.dp))

            ruta?.let {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Visualitzar Ruta",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF9DFFE8))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ruta ${ruta.id}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.Black
                            )

                            Text(
                                text = "Data: ${ruta.dataCreacio}",
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(380.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                GoogleMap(
                                    modifier = Modifier.fillMaxSize(),
                                    cameraPositionState = cameraState,
                                    uiSettings = MapUiSettings(zoomControlsEnabled = true,
                                        scrollGesturesEnabled = true,
                                        zoomGesturesEnabled = true)
                                ) {
                                    Polyline(points = puntsRuta, width = 6f, color = Color.Red)

                                    puntsRuta.forEach { punt ->
                                        Marker(
                                            state = rememberMarkerState(position = punt),
                                            title = "Lat: ${punt.latitude}, Lng: ${punt.longitude}"
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Distància", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("${"%.2f".format(ruta.distancia / 1000)} km", fontSize = 16.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Temps", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("${"%.2f".format(ruta.tempsTotal)} h", fontSize = 16.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Velocitat Mitja", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("${"%.2f".format(ruta.velocitatMitjana)} km/h", fontSize = 16.sp)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Velocitat Màx.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    val maxVelPermesa = parametres?.velocitatMaxima?.toDouble() ?: 20.0
                                    Text(
                                        text = "${"%.2f".format(ruta.velocitatMaxima)} km/h",
                                        color = if (ruta.velocitatMaxima > maxVelPermesa) Color.Red else Color.Black,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.coin_icon),
                                    contentDescription = "Icona monedas",
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "%.2f".format(ruta.saldoObtingut),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (ruta.validada) "Validada" else "No Validada",
                                color = if (ruta.validada) Color(0xFF4CAF50) else Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
        BottomSection(navController, userViewModel, 0)
    }
}
package cat.copernic.ymelero.entrebicis.rutes.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun LlistaRutesScreen(navController: NavController, userViewModel: UserViewModel) {
    val rutaViewModel = remember { RutaViewModel(RutaUseCases(RutaRepository())) }
    val llistaRutes by rutaViewModel.llistaRutes.collectAsState()
    val usuari by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        usuari?.email?.let {
            rutaViewModel.carregarRutesUsuari(it)
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
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Llista rutes",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                itemsIndexed(llistaRutes) { index, ruta ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF9DFFE8))
                            .padding(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ruta ${index + 1}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(
                                text = "Data: ${ruta.dataCreacio}",
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            InfoRow("Distància", "${"%.2f".format(ruta.distancia)} m", "Temps", "${"%.2f".format(ruta.tempsTotal)} h")
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow("Velocitat Mitja", "${"%.2f".format(ruta.velocitatMitjana)} km/h", "Velocitat Màx.", "${"%.2f".format(ruta.velocitatMaxima)} km/h")

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = R.drawable.coin_icon),
                                        contentDescription = "Icona monedes",
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = " ${"%.2f".format(ruta.saldoObtingut)}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1976D2),
                                        fontSize = 18.sp
                                    )
                                }
                                Text(
                                    text = if (ruta.validada) "Validada" else "No Validada",
                                    color = if (ruta.validada) Color(0xFF4CAF50) else Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(onClick = {
                                navController.navigate("detallsRuta/${ruta.id}")
                            }) {
                                Text("Veure Detalls")
                            }
                        }
                    }
                }
            }
        }
        BottomSection(navController, userViewModel, 0)
    }
}

@Composable
fun InfoRow(label1: String, value1: String, label2: String, value2: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label1, fontWeight = FontWeight.Bold)
            Text(value1)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label2, fontWeight = FontWeight.Bold)
            Text(value2)
        }
    }
}

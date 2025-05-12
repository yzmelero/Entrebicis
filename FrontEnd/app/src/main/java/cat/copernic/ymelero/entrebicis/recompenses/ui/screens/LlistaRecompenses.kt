package cat.copernic.ymelero.entrebicis.recompenses.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.core.model.EstatRecompensa
import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModel
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModelFactory
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun LlistaRecompensesScreen(navController: NavController, userViewModel: UserViewModel) {
    val recUseCase = RecUseCases(RecRepository())
    val recViewModel: RecViewModel = viewModel(factory = RecViewModelFactory(recUseCase))
    val recompenses by recViewModel.recompensesDisponibles.collectAsState()

    LaunchedEffect(Unit) {
        recViewModel.llistaRecompensesDisponibles()
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
            Column(  modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Llista recompenses",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    recompenses.forEach { recompensa ->
                        RecompensaCard(
                            recompensa = recompensa,
                            recViewModel = recViewModel,
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set("recompensaId", recompensa.id)
                                navController.navigate("detallRecompensa")
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        BottomSection(navController, userViewModel, 1)
    }
}

fun estatColor(estat: EstatRecompensa): Color {
    return when (estat) {
        EstatRecompensa.DISPONIBLE -> Color(0xFF007E33)
        EstatRecompensa.RESERVADA -> Color(0xFFFFA000)
        EstatRecompensa.ASSIGNADA -> Color(0xFF1976D2)
        EstatRecompensa.RECOLLIDA -> Color(0xFF9E9E9E)
    }
}

@Composable
fun RecompensaCard(recompensa: Recompensa, recViewModel: RecViewModel, onClick: () -> Unit = {}
) {
    val imageBitmap = recompensa.foto?.let { recViewModel.base64ToBitmap(it) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF9DFFE8))
            .clickable { onClick() }
    ) {
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = recompensa.descripcio,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.iconreward),
                contentDescription = "Imatge per defecte",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = recompensa.descripcio,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recompensa.nomComerc,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Text(
                text = "Data creació: ${recompensa.dataCreacio ?: "N/A"}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            if (recompensa.dataReserva != null) {
                Text(
                    text = "Data reserva: ${recompensa.dataReserva}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            if (recompensa.dataAssignacio != null) {
                Text(
                    text = "Data assignació: ${recompensa.dataAssignacio}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            if (recompensa.dataRecollida != null) {
                Text(
                    text = "Data recollida: ${recompensa.dataRecollida}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (recompensa.estat.name in listOf("RESERVADA", "ASSIGNADA", "RECOLLIDA")) {
                recompensa.usuari?.let { usuari ->
                    Text(
                        text = "Usuari: ${usuari.nom} ${usuari.cognoms}",
                        fontSize = 16.sp,
                        color = Color(0xFF444444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recompensa.estat.name,
                    fontWeight = FontWeight.Bold,
                    color = estatColor(recompensa.estat),
                    fontSize = 18.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${recompensa.punts.toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.coin_icon),
                        contentDescription = "Icona moneda",
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(20.dp)
                    )
                }
            }
        }
    }
}
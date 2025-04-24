package cat.copernic.ymelero.entrebicis.recompenses.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModel
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModelFactory
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun LlistaRecompensesScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
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
                .background(Color(0xFFB3F0F8))
                .windowInsetsPadding(WindowInsets.systemBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(navController, userViewModel)
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Llista recompenses",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            androidx.compose.foundation.lazy.LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(recompenses) { recompensa ->
                    RecompensaCard(recompensa, recViewModel)
                }
            }
        }
        BottomSection(navController, userViewModel, 1)
    }
}

@Composable
fun RecompensaCard(recompensa: Recompensa, recViewModel: RecViewModel) {
    val imageBitmap = recompensa.foto?.let { recViewModel.base64ToBitmap(it) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF98E0D6), shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = recompensa.descripcio,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = recompensa.nomComerc,
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = recompensa.descripcio,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recompensa.estat.name.capitalize(),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007E33)
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

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { /* TO DO: Detall */ }) {
                Text(text = "Veure Details")
            }
        }
    }
}
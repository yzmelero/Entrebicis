package cat.copernic.ymelero.entrebicis.recompenses.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.core.ui.theme.BlauTextTitol
import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModel
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModelFactory
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun RecollirRecompensaScreen(navController: NavController, userViewModel: UserViewModel) {
    val recViewModel: RecViewModel = viewModel(factory = RecViewModelFactory(RecUseCases(RecRepository())))
    val recompensa by recViewModel.recompensa.collectAsState()
    val context = LocalContext.current
    val missatgeRecollida by recViewModel.missatgeRecollida.collectAsState()

    val recompensaId = navController.previousBackStackEntry
        ?.savedStateHandle?.get<Long>("recompensaId") ?: return

    val usuari by userViewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        recViewModel.carregarRecompensaPerId(recompensaId)
    }

    LaunchedEffect(missatgeRecollida) {
        missatgeRecollida?.let {
            if (it == "ENTREGAT") {
                Toast.makeText(context, "Recompensa recollida amb èxit!", Toast.LENGTH_LONG).show()
                recViewModel.resetMissatgeRecollida()
            }
        }
    }

    recompensa?.let { r ->
        val imageBitmap = r.foto?.let { recViewModel.base64ToBitmap(it) }
        Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Recollir Recompensa",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlauTextTitol
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF9DFFE8))
                        .padding(28.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = r.descripcio,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Comerç: ${r.nomComerc}",
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    if (imageBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .padding(4.dp)
                        ) {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = r.descripcio,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(230.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    if (r.estat.name != "RECOLLIDA" && missatgeRecollida != "ENTREGAT") {
                        Button(
                            onClick = { recViewModel.recollirRecompensa(r.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "ENTREGAR",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (r.estat.name == "RECOLLIDA" || missatgeRecollida == "ENTREGAT") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(color = Color(0xFFB2DFDB))
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ENTREGAT",
                            fontSize = 54.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF004D40)
                        )
                    }
                }
            }

            BottomSection(navController, userViewModel, 1)
        }
    }
}

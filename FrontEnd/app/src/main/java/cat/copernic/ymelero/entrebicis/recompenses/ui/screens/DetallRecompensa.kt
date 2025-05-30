package cat.copernic.ymelero.entrebicis.recompenses.ui.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.core.ui.theme.BlauTextTitol
import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModel
import cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel.RecViewModelFactory
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun DetallRecompensaScreen(navController: NavController, userViewModel: UserViewModel) {
    val recompensaId = navController.previousBackStackEntry
        ?.savedStateHandle?.get<Long>("recompensaId") ?: return

    val recViewModel: RecViewModel = viewModel(factory = RecViewModelFactory(RecUseCases(RecRepository())))
    val recompensa by recViewModel.recompensa.collectAsState()
    val usuari by userViewModel.currentUser.collectAsState()

    val context = LocalContext.current
    val missatgeReserva by recViewModel.missatgeReserva.collectAsState()

    LaunchedEffect(missatgeReserva) {
        missatgeReserva?.let {
            if (it.isNotBlank()) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                recViewModel.resetMissatgeReserva()
            }
        }
    }
    LaunchedEffect(recompensaId) {
        Log.d("DEBUG", "Carregant recompensa amb ID $recompensaId")
        recViewModel.carregarRecompensaPerId(recompensaId)
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
                    text = r.descripcio,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BlauTextTitol
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(28.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = r.nomComerc,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF222222)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = r.adrecaComerc,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF555555)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.iconreward),
                                contentDescription = "Imatge per defecte",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(RoundedCornerShape(16.dp))

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = r.estat.name,
                            fontWeight = FontWeight.Bold,
                            color = estatColor(r.estat),
                            fontSize = 18.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${r.punts.toInt()}",
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Dates",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF444444),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    DetallText("Data de creació: ", r.dataCreacio.toString())
                    r.dataReserva?.let { DetallText("Data de reserva: ", it) }
                    r.dataAssignacio?.let { DetallText("Data d'assignació: ", it) }
                    r.dataRecollida?.let { DetallText("Data de recollida: ", it) }

                    Spacer(modifier = Modifier.height(20.dp))
                    if (r.estat.name == "DISPONIBLE") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF6AD0D3))
                                .clickable {
                                    usuari?.let { u ->
                                        recViewModel.reservarRecompensa(r.id, u.email, u.saldo)
                                    }
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Reservar",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                    if (r.estat.name == "ASSIGNADA") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF1976d2))
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("recompensaId", r.id)
                                    navController.navigate("recollirRecompensa")
                                }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Recollir",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
            BottomSection(navController, userViewModel, 1)
        }
    }
}

@Composable
fun DetallText(titol: String, valor: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Row {
            Text(
                text = titol,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = valor,
                fontSize = 16.sp,
                color = Color(0xFF333333)
            )
        }
    }
}

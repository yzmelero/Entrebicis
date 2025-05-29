package cat.copernic.ymelero.entrebicis.recompenses.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun LlistaRecompensesPropiesScreen(navController: NavController, userViewModel: UserViewModel) {
    val recUseCase = RecUseCases(RecRepository())
    val recViewModel: RecViewModel = viewModel(factory = RecViewModelFactory(recUseCase))
    val recompenses by recViewModel.recompensesPropies.collectAsState()

    val currentUser by userViewModel.currentUser.collectAsState()

    LaunchedEffect(currentUser?.email) {
        currentUser?.email?.let { email ->
            recViewModel.llistaRecompensesPropies(email)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Recompenses Propies",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlauTextTitol,
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

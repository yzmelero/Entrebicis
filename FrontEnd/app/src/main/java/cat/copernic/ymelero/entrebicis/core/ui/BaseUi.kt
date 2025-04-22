package cat.copernic.ymelero.entrebicis.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.core.ui.theme.BottomNavBar
import cat.copernic.ymelero.entrebicis.core.ui.theme.TopBar
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun header(navController: NavController, userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       TopBar(navController, userViewModel)
    }
}

@Composable
fun BottomSection(navController: NavController, userViewModel: UserViewModel, posicion: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Bottom
    ) {
        BottomNavBar(navController, userViewModel, selectedItem = posicion)
    }
}
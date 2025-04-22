package cat.copernic.ymelero.entrebicis.core.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun BottomNavBar(navController: NavController, userViewModel: UserViewModel, selectedItem: Int = 0) {
    NavigationBar(containerColor = Color(0xFF90CAF9)) {
    val currentUser by userViewModel.currentUser.collectAsState()

    val icons = listOf(
        R.drawable.map_icon,
        R.drawable.reward_icon,
        R.drawable.logoentrebicissin,
        R.drawable.user_icon,
        R.drawable.logout_icon
    )

    val destinations = listOf(
        "llistaRutes",
        "llistaRecompenses",
        "iniciarRuta",
        "usuari/${currentUser?.email}",
        "logout"
    )

        icons.forEachIndexed { index, iconRes ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    if (destinations[index] == "logout") {
                        userViewModel.logoutUser()
                        navController.navigate("login")
                    } else {
                        navController.navigate(destinations[index])
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}
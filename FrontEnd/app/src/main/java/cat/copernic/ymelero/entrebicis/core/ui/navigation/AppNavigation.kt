package cat.copernic.ymelero.entrebicis.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cat.copernic.ymelero.entrebicis.usuaris.ui.screens.LoginScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.screens.MenuScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        composable("menu") { MenuScreen(navController, userViewModel)}
    }
}
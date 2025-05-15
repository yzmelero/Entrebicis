package cat.copernic.ymelero.entrebicis.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.copernic.ymelero.entrebicis.recompenses.ui.screens.DetallRecompensaScreen
import cat.copernic.ymelero.entrebicis.recompenses.ui.screens.LlistaRecompensesPropiesScreen
import cat.copernic.ymelero.entrebicis.recompenses.ui.screens.LlistaRecompensesScreen
import cat.copernic.ymelero.entrebicis.recompenses.ui.screens.RecollirRecompensaScreen
import cat.copernic.ymelero.entrebicis.rutes.ui.screens.DetallsRutaScreen
import cat.copernic.ymelero.entrebicis.rutes.ui.screens.IniciRutaScreen
import cat.copernic.ymelero.entrebicis.rutes.ui.screens.LlistaRutesScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.screens.LoginScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.screens.ModificarUsuariScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.screens.UsuariScreen
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun AppNavigation(userViewModel: UserViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, userViewModel) }
        //composable("menu") { MenuScreen(navController, userViewModel)}
        composable("llistaRutes") { LlistaRutesScreen(navController, userViewModel) }
        composable("llistaRecompenses") { LlistaRecompensesScreen(navController, userViewModel) }
        composable("iniciarRuta") { IniciRutaScreen(navController, userViewModel) }
        composable("usuari/{email}") {  UsuariScreen(navController, userViewModel) }
        composable("llistaRecompensesPropies") { LlistaRecompensesPropiesScreen(navController, userViewModel) }
        composable("modificarUsuari") { ModificarUsuariScreen(navController, userViewModel) }
        composable("detallRecompensa") { DetallRecompensaScreen(navController, userViewModel) }
        composable("detallsRuta/{idRuta}") { DetallsRutaScreen(navController, userViewModel)}
        composable("recollirRecompensa") { RecollirRecompensaScreen(navController, userViewModel)}

    }
}
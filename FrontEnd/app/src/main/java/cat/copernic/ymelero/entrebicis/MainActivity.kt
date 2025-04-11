package cat.copernic.ymelero.entrebicis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.ymelero.entrebicis.core.ui.navigation.AppNavigation
import cat.copernic.ymelero.entrebicis.core.ui.theme.EntrebicisTheme
import cat.copernic.ymelero.entrebicis.usuaris.data.UserRepository
import cat.copernic.ymelero.entrebicis.usuaris.domain.UseCases
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val useCases = UseCases(UserRepository())
            val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(useCases))
            AppNavigation(userViewModel)
        }
    }
}
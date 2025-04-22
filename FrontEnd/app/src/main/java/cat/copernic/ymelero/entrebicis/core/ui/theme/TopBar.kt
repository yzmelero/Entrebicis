package cat.copernic.ymelero.entrebicis.core.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun TopBar(navController: NavController, userViewModel: UserViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF90CAF9))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.iconuser),
            contentDescription = "Avatar de usuario",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White, shape = CircleShape)
                .padding(2.dp)
                .clickable {
                    //navController.navigate("perfil/${currentUser?.username}")
                }
        )

        Text(
            text = "EntreBicis",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Icon(
            Icons.Default.ExitToApp,
            contentDescription = stringResource(R.string.logout),
            Modifier
                .size(40.dp)
                .clickable {
                    userViewModel.logoutUser()
                    navController.navigate("login")
                }
        )
    }
}

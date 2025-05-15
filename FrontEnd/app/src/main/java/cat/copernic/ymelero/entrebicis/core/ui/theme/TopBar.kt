package cat.copernic.ymelero.entrebicis.core.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun TopBar(navController: NavController, userViewModel: UserViewModel) {
    val currentUser by userViewModel.currentUser.collectAsState()
    val userImageBitmap = currentUser?.foto?.let { userViewModel.base64ToBitmap(it) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF90CAF9))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (userImageBitmap != null) {
            Image(
                bitmap = userImageBitmap,
                contentDescription = "Avatar de l'usuari",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("usuari/${currentUser?.email}")
                    }
            )
        } else {
            Image(
                painter = painterResource(R.drawable.iconuser),
                contentDescription = "Avatar per defecte",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("usuari/${currentUser?.email}")
                    }
            )
        }

        Text(
            text = "EntreBicis",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                userViewModel.refrescarUsuari()
                Toast.makeText(
                    navController.context,
                    "Dades actualitzades",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.coin_icon),
                contentDescription = "Icona monedas",
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "${currentUser?.saldo?.toInt() ?: 0}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

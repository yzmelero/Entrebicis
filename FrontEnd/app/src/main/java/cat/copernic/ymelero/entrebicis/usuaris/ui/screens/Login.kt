package cat.copernic.ymelero.entrebicis.usuaris.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.usuaris.data.UserRepository
import cat.copernic.ymelero.entrebicis.usuaris.domain.UseCases
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }

    val loginSuccess by userViewModel.loginSuccess.collectAsState()

    LaunchedEffect(loginSuccess) {
        if (loginSuccess == true) {
            navController.navigate("iniciarRuta")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFB3F0F8)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(52.dp))
        Text(
            text = "EntreBicis",
            fontSize = 46.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.logoentrebicissin),
            contentDescription = "Logo bici",
            modifier = Modifier
                .size(250.dp)
                .padding(top = 8.dp, bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(0.85f),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Inici Sessió",
                    fontSize = 24.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = contrasenya,
                    onValueChange = { contrasenya = it },
                    label = { Text("Contrasenya") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        userViewModel.loginUser(email, contrasenya)},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBEE1F4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(45.dp)
                ) {
                    Text("Confirmar", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = {
                    navController.navigate("oblidatContrasenya") /* TODO OblidatContrasenya */
                }) {
                    Text("Has oblidat la contrasenya?", color = Color(0xFF1A73E8))
                }
                val context = LocalContext.current
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.entrebicis.org/formuluari-club"))
                        context.startActivity(intent)
                              },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBEE1F4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(40.dp)
                ) {
                    Text("Formulari Alta", color = Color.Black)
                }
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    val fakeNavController =
        rememberNavController()
    LoginScreen(
        navController = fakeNavController,
        userViewModel = UserViewModel(UseCases(UserRepository()))
    )
}

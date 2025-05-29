package cat.copernic.ymelero.entrebicis.usuaris.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.core.ui.BottomSection
import cat.copernic.ymelero.entrebicis.core.ui.header
import cat.copernic.ymelero.entrebicis.core.ui.theme.BlauClarEntreBicis
import cat.copernic.ymelero.entrebicis.core.ui.theme.BlauTextTitol
import cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel.UserViewModel

@Composable
fun ModificarUsuariScreen(navController: NavController, userViewModel: UserViewModel) {
    val context = LocalContext.current
    val updateSuccess by userViewModel.updateSuccess.collectAsState()
    val updateError by userViewModel.updateError.collectAsState()
    val usuari by userViewModel.currentUser.collectAsState()

    var nom by remember { mutableStateOf("") }
    var cognoms by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var poblacio by remember { mutableStateOf("") }
    var contrasenya by remember { mutableStateOf("") }
    var repetirContrasenya by remember { mutableStateOf("") }
    var fotoBase64 by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            userViewModel._updateSuccess.value = null
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    LaunchedEffect(usuari) {
        usuari?.let {
            nom = it.nom
            cognoms = it.cognoms
            email = it.email
            telefon = it.telefon
            poblacio = it.poblacio
            fotoBase64 = it.foto ?: ""
        }
    }

    val imageBitmap = if (selectedImageUri != null) {
        userViewModel.uriToBase64(context, selectedImageUri!!)?.let {
            fotoBase64 = it
            userViewModel.base64ToBitmap(it)
        }
    } else {
        userViewModel.base64ToBitmap(fotoBase64)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlauClarEntreBicis)
                .windowInsetsPadding(WindowInsets.systemBars)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header(navController, userViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            Text("Modificar Usuari", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BlauTextTitol)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .background(Color.White, shape = RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(value = nom, onValueChange = { nom = it }, label = { Text("Nom") })
                OutlinedTextField(value = cognoms, onValueChange = { cognoms = it }, label = { Text("Cognoms") })
                OutlinedTextField(value = email, onValueChange = {}, label = { Text("Email") }, enabled = false)
                OutlinedTextField(value = telefon, onValueChange = { telefon = it }, label = { Text("Telèfon") })
                OutlinedTextField(value = poblacio, onValueChange = { poblacio = it }, label = { Text("Població") })
                OutlinedTextField(
                    value = contrasenya,
                    onValueChange = { contrasenya = it },
                    label = { Text("Contrasenya") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(
                    value = repetirContrasenya,
                    onValueChange = { repetirContrasenya = it },
                    label = { Text("Repetir Contrasenya") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(contentAlignment = Alignment.BottomEnd) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.iconuser),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(120.dp).clip(CircleShape)
                        )
                    }

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Canviar imatge",
                        modifier = Modifier
                            .clickable {
                                launcher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .background(Color(0xFF7A5FFF), CircleShape)
                            .padding(6.dp)
                            .size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    usuari?.let { user ->
                        val novaContrasenya =
                            if (contrasenya.isBlank() && repetirContrasenya.isBlank()) {
                                user.contrasenya
                            } else {
                                if (contrasenya == repetirContrasenya) {
                                    contrasenya
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Les contrasenyes no coincideixen",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@let
                                }
                            }

                        userViewModel.updateUser(
                            user.copy(
                                nom = nom,
                                cognoms = cognoms,
                                telefon = telefon,
                                poblacio = poblacio,
                                contrasenya = novaContrasenya,
                                foto = fotoBase64
                            )
                        )
                    }
                }) {
                    Text("Confirmar", fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
        BottomSection(navController, userViewModel, 3)
    }

    LaunchedEffect(updateSuccess) {
        if (updateSuccess == true) {
            Toast.makeText(context, "Usuari actualitzat correctament", Toast.LENGTH_LONG).show()
            navController.popBackStack()
        }
    }
    LaunchedEffect(updateError) {
        updateError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            userViewModel.clearError()
        }
    }
}

package cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import cat.copernic.ymelero.entrebicis.usuaris.domain.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class UserViewModel(private val useCases: UseCases) : ViewModel() {
    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _currentUser = MutableStateFlow<Usuari?>(null)
    val currentUser: StateFlow<Usuari?> get() = _currentUser

    fun loginUser(email: String, contrasenya: String) {
        viewModelScope.launch {
            try {
                val loginResponse = useCases.loginUser(email, contrasenya)
                if (loginResponse.isSuccessful) {
                    val userResponse = useCases.getUsuari(email)
                    if (userResponse.isSuccessful) {
                        val user = userResponse.body()
                        _currentUser.value = user
                        _loginSuccess.value = true
                        _loginError.value = null
                    } else {
                        _currentUser.value = null
                        _loginSuccess.value = false
                        _loginError.value = "No s'ha pogut obtenir l'usuari."
                    }
                } else {
                    val error = loginResponse.errorBody()?.string() ?: "Error d'autenticació"
                    _loginSuccess.value = false
                    _loginError.value = error
                }
            } catch (e: Exception) {
                _loginSuccess.value = false
                _loginError.value = "${e.message}"
            }
        }
    }


    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun clearLoginError() {
        _loginError.value = null
    }

    fun logoutUser() {
        _currentUser.value = null
        _loginSuccess.value = false
    }

    fun obtenirUsuari(usuari: Usuari) {
        _currentUser.value = usuari
    }

    val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    fun updateUser(usuari: Usuari) {
        viewModelScope.launch {
            try {
                val updateResponse = useCases.updateUsuari(usuari)
                if (updateResponse.isSuccessful && updateResponse.body() != null) {
                    _currentUser.value = updateResponse.body()!!
                    _updateSuccess.value = true
                    _updateError.value = null
                } else {
                    val errorMsg = updateResponse.errorBody()?.string() ?: "Error desconegut"
                    _updateError.value = errorMsg
                    _updateSuccess.value = false
                }
            } catch (e: Exception) {
                _updateError.value = "${e.message}"
                _updateSuccess.value = false
            }
        }
    }

    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> = _updateError

    fun clearError() {
        _updateError.value = null
    }



    fun base64ToBitmap(base64: String): ImageBitmap? {
        return try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            val byteArrayInputStream = ByteArrayInputStream(decodedBytes)
            val bitmap = BitmapFactory.decodeStream(byteArrayInputStream)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()

            inputStream?.use { stream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                }
            }

            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
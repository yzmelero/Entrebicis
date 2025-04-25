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
            val loginResponse = useCases.loginUser(email, contrasenya)
            _loginSuccess.value = loginResponse.isSuccessful
            if (loginResponse.isSuccessful) {
                val userResponse = useCases.getUsuari(email)
                if (userResponse.isSuccessful) {
                    val user = userResponse.body()
                    _currentUser.value = user
                    _loginSuccess.value = true
                } else {
                    _currentUser.value = null
                    _loginSuccess.value = false
                }
            }
        }
    }

    fun logoutUser() {
        _currentUser.value = null
        _loginSuccess.value = false
    }

    fun obtenirUsuari(usuari: Usuari) {
        _currentUser.value = usuari
    }

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    fun updateSuccess(usuari: Usuari) {
        viewModelScope.launch {
            try {
                val updateResponse = useCases.updateUsuari(usuari)
                if (updateResponse.isSuccessful && updateResponse.body() != null) {
                    _currentUser.value = updateResponse.body()!!
                    _updateSuccess.value = true
                } else {
                    _updateSuccess.value = false
                }
            } catch (e: Exception) {
                _updateSuccess.value = false
            }
        }
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
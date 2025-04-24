package cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel

import android.graphics.BitmapFactory
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

    fun ObtenirUsuari(usuari: Usuari) {
        _currentUser.value = usuari
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

}
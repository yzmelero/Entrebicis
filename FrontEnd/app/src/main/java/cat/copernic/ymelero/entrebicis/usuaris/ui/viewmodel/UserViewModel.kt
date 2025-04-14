package cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import cat.copernic.ymelero.entrebicis.usuaris.domain.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                    val user = userResponse.body()

                    _currentUser.value = user
                    _loginSuccess.value = true
                } else {
                    _loginSuccess.value = false
                }

            } catch (e: Exception) {
                _loginSuccess.value = false
            }
        }
    }

    fun ObtenirUsuari(usuari: Usuari) {
        _currentUser.value = usuari
    }

}
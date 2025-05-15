package cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

class RecViewModel(private val recUseCases: RecUseCases) : ViewModel() {

    private val _recompensesDisponibles = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensesDisponibles: StateFlow<List<Recompensa>> get() = _recompensesDisponibles

    fun llistaRecompensesDisponibles() {
        viewModelScope.launch {
            try {
                val response = recUseCases.getRecompensesDisponibles()
                if (response.isSuccessful && response.body() != null) {
                    _recompensesDisponibles.value = response.body()!!
                } else {
                    Log.e("RecViewModel", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                _recompensesDisponibles.value = emptyList()
            }
        }
    }

    private val _recompensesPropies = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensesPropies: StateFlow<List<Recompensa>> get() = _recompensesPropies

    fun llistaRecompensesPropies(email: String) {
        viewModelScope.launch {
            try {
                val response = recUseCases.getRecompensesPropies(email)
                if (response.isSuccessful && response.body() != null) {
                    _recompensesPropies.value = response.body()!!
                } else {
                    Log.e("RecViewModel", "Error propies: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _recompensesPropies.value = emptyList()
            }
        }
    }

    private val _recompensa = MutableStateFlow<Recompensa?>(null)
    val recompensa: StateFlow<Recompensa?> get() = _recompensa

    fun carregarRecompensaPerId(id: Long) {
        viewModelScope.launch {
            try {
                val response = recUseCases.getRecompensaPerId(id)
                if (response.isSuccessful) {
                    _recompensa.value = response.body()
                } else {
                    Log.e("RecViewModel", "Error detall: ${response.code()} - ${response.errorBody()?.string()}")
                    _recompensa.value = null
                }
            } catch (e: Exception) {
                Log.e("RecViewModel", "Excepció carregant detall: ${e.message}")
                _recompensa.value = null
            }
        }
    }

    private val _missatgeReserva = MutableStateFlow<String?>(null)
    val missatgeReserva: StateFlow<String?> get() = _missatgeReserva

    fun reservarRecompensa(recompensaId: Long, email: String, saldo: Double) {
        viewModelScope.launch {
            try {
                val response = recUseCases.reservarRecompensa(recompensaId, email, saldo)
                if (response.isSuccessful) {
                    _recompensa.value = response.body()
                    _missatgeReserva.value = "Recompensa reservada amb èxit!"
                } else {
                    val errorText = response.body()?.toString() ?: response.errorBody()?.string()
                    _missatgeReserva.value = errorText
                }
            } catch (e: Exception) {
                _missatgeReserva.value = "Error: ${e.message}"
            }
        }
    }
    fun resetMissatgeReserva() {
        _missatgeReserva.value = null
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
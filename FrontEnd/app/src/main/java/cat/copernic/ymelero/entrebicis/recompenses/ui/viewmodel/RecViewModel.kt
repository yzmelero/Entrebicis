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
                    Log.d("RecViewModel", "Response: ${response.body()}")

                    _recompensesDisponibles.value = response.body()!!
                } else {
                    Log.e("RecViewModel", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                _recompensesDisponibles.value = emptyList()
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
}
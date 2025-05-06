package cat.copernic.ymelero.entrebicis.parametres.ui.viewmodel

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import cat.copernic.ymelero.entrebicis.parametres.domain.ParamUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream

class ParamViewModel(private val paramUseCases: ParamUseCases) : ViewModel() {

    private val _tempsMaximAturada = MutableStateFlow(5)
    val tempsMaximAturada: StateFlow<Int> get() = _tempsMaximAturada

    fun carregarTempsMaximAturada() {
        viewModelScope.launch {
            try {
                val response = paramUseCases.getTempsMaximAturada()
                if (response.isSuccessful && response.body() != null) {
                    _tempsMaximAturada.value = response.body()!!
                }
            } catch (e: Exception) {
                // manté el valor per defecte
            }
        }
    }
}
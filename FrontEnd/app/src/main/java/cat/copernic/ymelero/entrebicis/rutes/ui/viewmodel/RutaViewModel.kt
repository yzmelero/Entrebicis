package cat.copernic.ymelero.entrebicis.rutes.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.EstatRuta
import cat.copernic.ymelero.entrebicis.core.model.Ruta
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import cat.copernic.ymelero.entrebicis.rutes.domain.RutaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutaViewModel(private val rutaUseCases: RutaUseCases) : ViewModel() {

    private val _rutaActual = MutableStateFlow<Ruta?>(null)
    val rutaActual: StateFlow<Ruta?> get() = _rutaActual

    fun iniciarRuta(usuari: Usuari) {
        viewModelScope.launch {
            try {
                val novaRuta = Ruta(
                    id = null,
                    usuari = usuari,
                    estat = EstatRuta.PENDENT,
                    distancia = 0.0,
                    tempsTotal = 0.0,
                    velocitatMaxima = 0.0,
                    velocitatMitjana = 0.0,
                    puntsGPS = null,
                    validada = false,
                    dataCreacio = ""
                )
                val response = rutaUseCases.iniciarRuta(novaRuta)
                if (response.isSuccessful) {
                    _rutaActual.value = response.body()
                } else {
                    Log.e("RutaUseCases","Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Excepció: ${e.message}")
            }
        }
    }
}
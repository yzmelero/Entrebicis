package cat.copernic.ymelero.entrebicis.rutes.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.ymelero.entrebicis.core.model.EstatRuta
import cat.copernic.ymelero.entrebicis.core.model.PuntGPS
import cat.copernic.ymelero.entrebicis.core.model.Ruta
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import cat.copernic.ymelero.entrebicis.rutes.domain.RutaUseCases
import com.google.android.gms.maps.model.LatLng
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
                    puntGPS = null,
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

    private val _rutaFinalitzada = MutableStateFlow<Ruta?>(null)
    val rutaFinalitzada: StateFlow<Ruta?> get() = _rutaFinalitzada

    fun finalitzarRuta() {
        val ruta = _rutaActual.value ?: return
        viewModelScope.launch {
            try {
                val response = rutaUseCases.finalitzarRuta(ruta.id!!)
                if (response.isSuccessful) {
                    _rutaFinalitzada.value = response.body()
                    _rutaActual.value = null
                    puntsRuta.clear()
                    Log.i("RutaViewModel", "Ruta finalitzada correctament")
                } else {
                    Log.e("RutaViewModel", "Error finalitzant: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Excepció finalitzant ruta: ${e.message}")
            }
        }
    }

    fun resetRutaFinalitzada() {
        _rutaFinalitzada.value = null
    }

    val puntsRuta = mutableStateListOf<LatLng>()

    fun afegirPuntGPS(lat: Double, lng: Double) {
        val ruta = _rutaActual.value ?: return
        puntsRuta.add(LatLng(lat, lng))
        viewModelScope.launch {
            try {
                val punt = PuntGPS(
                    id = null,
                    latitud = lat,
                    longitud = lng,
                    marcaTemps = ""
                )
                rutaUseCases.afegirPuntGPS(ruta.id!!, punt)
                Log.i("RutaViewModel", "Punt GPS afegit")
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Error afegint punt GPS: ${e.message}")
            }
        }
    }

    private val _rutaCarregada = MutableStateFlow<Ruta?>(null)
    val rutaCarregada: StateFlow<Ruta?> get() = _rutaCarregada

    fun carregarRutaPerId(idRuta: Long) {
        viewModelScope.launch {
            try {
                val response = rutaUseCases.obtenirRuta(idRuta)
                if (response.isSuccessful) {
                    _rutaCarregada.value = response.body()
                    Log.i("RutaViewModel", "Ruta carregada correctament")
                } else {
                    Log.e("RutaViewModel", "Error carregant ruta: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RutaViewModel", "Excepció carregant ruta: ${e.message}")
            }
        }
    }

}
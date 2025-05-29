package cat.copernic.ymelero.entrebicis.rutes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.ymelero.entrebicis.rutes.domain.RutaUseCases

class RutaViewModelFactory(private val rutaUseCases: RutaUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RutaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RutaViewModel(rutaUseCases) as T
        }
        throw IllegalArgumentException("ViewModel no reconegut")
    }
}
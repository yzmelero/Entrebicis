package cat.copernic.ymelero.entrebicis.recompenses.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases

class RecViewModelFactory(private val recUseCases: RecUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecViewModel(recUseCases) as T
        }
        throw IllegalArgumentException("ViewModel no reconegut")
    }
}
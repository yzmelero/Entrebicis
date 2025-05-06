package cat.copernic.ymelero.entrebicis.parametres.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.ymelero.entrebicis.parametres.domain.ParamUseCases
import cat.copernic.ymelero.entrebicis.recompenses.domain.RecUseCases

class ParamViewModelFactory(private val paramUseCases: ParamUseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParamViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParamViewModel(paramUseCases) as T
        }
        throw IllegalArgumentException("ViewModel no reconegut")
    }
}
package cat.copernic.ymelero.entrebicis.usuaris.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.ymelero.entrebicis.R
import cat.copernic.ymelero.entrebicis.usuaris.domain.UseCases

class UserViewModelFactory(private val useCases: UseCases) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(useCases) as T
        }
        throw IllegalArgumentException(R.string.unkViewModel.toString())
    }
}
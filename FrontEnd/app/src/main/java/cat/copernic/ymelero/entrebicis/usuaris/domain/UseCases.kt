package cat.copernic.ymelero.entrebicis.usuaris.domain

import cat.copernic.ymelero.entrebicis.core.model.Usuari
import cat.copernic.ymelero.entrebicis.usuaris.data.UserRepository

class UseCases(private val repository: UserRepository) {

    suspend fun getUsuari(email: String) = repository.getUser(email)

    suspend fun loginUser(email: String, contrasenya: String) = repository.loginUser(email, contrasenya)
    
    suspend fun updateUsuari(usuari: Usuari) = repository.updateUser(usuari)
}
package cat.copernic.ymelero.entrebicis.usuaris.data

import cat.copernic.ymelero.entrebicis.core.model.LoginRequest
import cat.copernic.ymelero.entrebicis.core.model.LoginResponse
import cat.copernic.ymelero.entrebicis.core.model.Parametres
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import retrofit2.Response

class UserRepository {

    suspend fun loginUser(email: String, contrasenya: String): Response<LoginResponse> {
        val request = LoginRequest(email, contrasenya)
        return RetrofitInstance.api.loginUser(request)
    }
    suspend fun getUser(email: String): Response<Usuari> {
        return RetrofitInstance.api.getUsuari(email)
    }
    suspend fun updateUser(usuari: Usuari): Response<Usuari> {
        return RetrofitInstance.api.modificarUsuari(usuari)
    }
    suspend fun getParametresSistema(): Response<Parametres> {
        return RetrofitInstance.api.getParametresSistema()
    }
}
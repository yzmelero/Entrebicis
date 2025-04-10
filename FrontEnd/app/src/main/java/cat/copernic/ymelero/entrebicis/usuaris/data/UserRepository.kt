package cat.copernic.ymelero.entrebicis.usuaris.data

import cat.copernic.ymelero.entrebicis.core.model.LoginRequest
import cat.copernic.ymelero.entrebicis.core.model.LoginResponse

class UserRepository {
    suspend fun loginUser(email: String, contrasenya: String): Response<LoginResponse> {
        val request = LoginRequest(email, contrasenya)
        return RetrofitInstance.loginApi.login(request)
    }
}
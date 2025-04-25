package cat.copernic.ymelero.entrebicis.usuaris.data

import cat.copernic.ymelero.entrebicis.core.model.LoginRequest
import cat.copernic.ymelero.entrebicis.core.model.LoginResponse
import cat.copernic.ymelero.entrebicis.core.model.Usuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiRest {
    @POST("api/login/verify")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/usuari/visualitzar/{email}")
    suspend fun getUsuari(@Path("email") email: String): Response<Usuari>

    @PUT("api/usuari/modificar")
    suspend fun modificarUsuari(@Body usuari: Usuari): Response<Usuari>

}
package cat.copernic.ymelero.entrebicis.usuaris.data

interface UserApiRest {
    @POST("login/verify")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
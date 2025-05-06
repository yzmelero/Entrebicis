package cat.copernic.ymelero.entrebicis.rutes.data

import cat.copernic.ymelero.entrebicis.core.model.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RutaApiRest {
    @POST("api/ruta/iniciar")
    suspend fun iniciarRuta(@Body ruta: Ruta): Response<Ruta>
}
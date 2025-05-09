package cat.copernic.ymelero.entrebicis.rutes.data

import cat.copernic.ymelero.entrebicis.core.model.PuntGPS
import cat.copernic.ymelero.entrebicis.core.model.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RutaApiRest {
    @POST("api/ruta/iniciar")
    suspend fun iniciarRuta(@Body ruta: Ruta): Response<Ruta>

    @PUT("api/ruta/{idRuta}/finalitzar")
    suspend fun finalitzarRuta(@Path("idRuta") idRuta: Long): Response<Ruta>

    @POST("api/ruta/{idRuta}/puntgps")
    suspend fun afegirPuntGPS(@Path("idRuta") idRuta: Long, @Body punt: PuntGPS): Response<PuntGPS>
}
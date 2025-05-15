package cat.copernic.ymelero.entrebicis.recompenses.data

import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface RecApiRest {
    @GET("api/recompenses/disponibles")
    suspend fun getRecompensesDisponibles(): Response<List<Recompensa>>

    @GET("api/recompenses/propies")
    suspend fun getRecompensesPropies(@Query("email") email: String): Response<List<Recompensa>>

    @GET("api/recompenses")
    suspend fun getRecompensaPerId(@Query("id") id: Long): Response<Recompensa>

    @POST("api/recompenses/reservar")
    suspend fun reservarRecompensa(@Query("id") id: Long, @Query("email") email: String, @Query("saldo") saldo: Double): Response<Recompensa>

    @PUT("api/recompenses/recollir")
    suspend fun recollirRecompensa(@Query("id") id: Long): Response<Recompensa>

}
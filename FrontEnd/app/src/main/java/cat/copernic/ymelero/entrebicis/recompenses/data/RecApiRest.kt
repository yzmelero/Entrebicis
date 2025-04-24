package cat.copernic.ymelero.entrebicis.recompenses.data

import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import retrofit2.Response
import retrofit2.http.GET

interface RecApiRest {
    @GET("api/recompenses/disponibles")
    suspend fun getRecompensesDisponibles(): Response<List<Recompensa>>
}
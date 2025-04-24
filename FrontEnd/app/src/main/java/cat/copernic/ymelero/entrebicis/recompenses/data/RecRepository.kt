package cat.copernic.ymelero.entrebicis.recompenses.data

import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import retrofit2.Response

class RecRepository {
    suspend fun getRecompensesDisponibles(): Response<List<Recompensa>> {
        return RetrofitInstance.api.getRecompensesDisponibles()
    }
}
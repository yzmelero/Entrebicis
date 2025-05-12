package cat.copernic.ymelero.entrebicis.recompenses.data

import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import retrofit2.Response

class RecRepository {
    suspend fun getRecompensesDisponibles(): Response<List<Recompensa>> {
        return RetrofitInstance.api.getRecompensesDisponibles()
    }

    suspend fun getRecompensesPropies(email: String): Response<List<Recompensa>> {
        return RetrofitInstance.api.getRecompensesPropies(email)
    }

    suspend fun getRecompensaPerId(id: Long): Response<Recompensa> {
        return RetrofitInstance.api.getRecompensaPerId(id)
    }

    suspend fun reservarRecompensa(recompensaId: Long, email: String, saldo: Double): Response<Recompensa> {
        return RetrofitInstance.api.reservarRecompensa(recompensaId, email, saldo)
    }

}
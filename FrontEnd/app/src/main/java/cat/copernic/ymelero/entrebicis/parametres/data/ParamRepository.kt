package cat.copernic.ymelero.entrebicis.parametres.data

import cat.copernic.ymelero.entrebicis.core.model.Recompensa
import retrofit2.Response

class ParamRepository {
    suspend fun getTempsMaximAturada(): Response<Int> {
        return RetrofitInstance.api.getTempsMaximAturada()
    }
}
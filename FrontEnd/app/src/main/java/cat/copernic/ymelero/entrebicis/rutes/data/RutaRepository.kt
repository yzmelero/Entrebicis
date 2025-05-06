package cat.copernic.ymelero.entrebicis.rutes.data

import cat.copernic.ymelero.entrebicis.core.model.Ruta
import retrofit2.Response

class RutaRepository {
    suspend fun iniciarRuta(ruta: Ruta): Response<Ruta> {
        return RetrofitInstance.api.iniciarRuta(ruta)
    }
}
package cat.copernic.ymelero.entrebicis.rutes.data

import cat.copernic.ymelero.entrebicis.core.model.PuntGPS
import cat.copernic.ymelero.entrebicis.core.model.Ruta
import retrofit2.Response

class RutaRepository {
    suspend fun iniciarRuta(ruta: Ruta): Response<Ruta> {
        return RetrofitInstance.api.iniciarRuta(ruta)
    }

    suspend fun finalitzarRuta(idRuta: Long): Response<Ruta> {
        return RetrofitInstance.api.finalitzarRuta(idRuta)
    }

    suspend fun afegirPuntGPS(idRuta: Long, punt: PuntGPS): Response<PuntGPS> {
        return RetrofitInstance.api.afegirPuntGPS(idRuta, punt)
    }

}
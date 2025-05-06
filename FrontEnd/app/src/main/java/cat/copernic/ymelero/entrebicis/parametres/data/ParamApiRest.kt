package cat.copernic.ymelero.entrebicis.parametres.data

import retrofit2.Response
import retrofit2.http.GET

interface ParamApiRest {

    @GET("api/parametres/aturada")
    suspend fun getTempsMaximAturada(): Response<Int>
}
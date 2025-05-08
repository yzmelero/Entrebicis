package cat.copernic.ymelero.entrebicis.core.model

data class Ruta (
    val id: Long? = 0L,
    val usuari: Usuari,
    val estat: EstatRuta,
    val distancia: Double,
    val tempsTotal: Double,
    val velocitatMaxima: Double,
    val velocitatMitjana: Double,
    val puntGPS: List<PuntGPS>?,
    val validada: Boolean,
    val dataCreacio: String
)
package cat.copernic.ymelero.entrebicis.core.model

data class Recompensa (
    val id: Long,
    val descripcio: String,
    val observacions: String?,
    val punts: Double,
    val estat: EstatRecompensa,
    val nomComerc: String,
    val adrecaComerc: String,
    val foto: String? = null,
    val usuari: Usuari? = null,
    val dataReserva: String? = null,
    val dataRecollida: String? = null,
    val dataCreacio: String? = null,
    val dataAssignacio: String? = null,
)
package cat.copernic.ymelero.entrebicis.core.model

data class Usuari (
    val email: String,
    val nom: String,
    val cognoms: String,
    val dataNaixement: String,
    val foto: String? = null,
    val poblacio: String,
    val telefon: String,
    val rol: Rol,
    val saldo: Double,
    val dataAlta: String,
    val contrasenya: String,
    val observacions: String? = null
)
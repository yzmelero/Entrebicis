package cat.copernic.ymelero.entrebicis.core.model

data class Parametres (
    val id: Long = 1,
    val velocitatMaxima: Int = 60,
    val tempsMaximAturada: Int = 5,
    val conversioQuilometreSaldo: Double = 1.0,
    val tempsMaximRecollidaRecompensa: Int = 72
)
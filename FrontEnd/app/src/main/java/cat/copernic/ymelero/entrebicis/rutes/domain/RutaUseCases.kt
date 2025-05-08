package cat.copernic.ymelero.entrebicis.rutes.domain

import cat.copernic.ymelero.entrebicis.core.model.PuntGPS
import cat.copernic.ymelero.entrebicis.core.model.Ruta
import cat.copernic.ymelero.entrebicis.rutes.data.RutaRepository

class RutaUseCases (private val rutaRepository: RutaRepository) {
    suspend fun iniciarRuta(ruta: Ruta) = rutaRepository.iniciarRuta(ruta)
    suspend fun afegirPuntGPS(idRuta: Long, punt: PuntGPS) = rutaRepository.afegirPuntGPS(idRuta, punt)
    suspend fun finalitzarRuta(idRuta: Long) = rutaRepository.finalitzarRuta(idRuta)

}
package cat.copernic.ymelero.entrebicis.rutes.domain

import cat.copernic.ymelero.entrebicis.core.model.Ruta
import cat.copernic.ymelero.entrebicis.rutes.data.RutaRepository

class RutaUseCases (private val rutaRepository: RutaRepository) {
    suspend fun iniciarRuta(ruta: Ruta) = rutaRepository.iniciarRuta(ruta)
}
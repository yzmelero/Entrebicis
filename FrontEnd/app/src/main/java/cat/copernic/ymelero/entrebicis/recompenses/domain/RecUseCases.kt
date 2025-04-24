package cat.copernic.ymelero.entrebicis.recompenses.domain

import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository

class RecUseCases (private val recRepository: RecRepository) {
    suspend fun getRecompensesDisponibles() = recRepository.getRecompensesDisponibles()
}
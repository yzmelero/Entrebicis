package cat.copernic.ymelero.entrebicis.recompenses.domain

import cat.copernic.ymelero.entrebicis.recompenses.data.RecRepository

class RecUseCases (private val recRepository: RecRepository) {
    suspend fun getRecompensesDisponibles() = recRepository.getRecompensesDisponibles()
    suspend fun getRecompensesPropies(email: String) = recRepository.getRecompensesPropies(email)
    suspend fun getRecompensaPerId(id: Long) = recRepository.getRecompensaPerId(id)
    suspend fun reservarRecompensa(recompensaId: Long, email: String, saldo: Double) =
        recRepository.reservarRecompensa(recompensaId, email, saldo)
    suspend fun recollirRecompensa(id: Long) = recRepository.recollirRecompensa(id)

}
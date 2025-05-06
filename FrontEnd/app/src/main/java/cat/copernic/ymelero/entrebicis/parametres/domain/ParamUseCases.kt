package cat.copernic.ymelero.entrebicis.parametres.domain

import cat.copernic.ymelero.entrebicis.parametres.data.ParamRepository

class ParamUseCases (private val paramRepository: ParamRepository) {
    suspend fun getTempsMaximAturada() = paramRepository.getTempsMaximAturada()
}
package br.com.fiap.eventsproducer.application.rest.dto

import br.com.fiap.eventsproducer.domain.entities.CreditEvaluation
import java.math.BigDecimal

data class CreditEvaluationDTO(
    val customerId: String?,
    val totalCredit: BigDecimal?
) {
    init {
        requireNotNull(customerId)
        requireNotNull(totalCredit)
    }
    companion object {
        fun toDomainObjet(dto: CreditEvaluationDTO) = CreditEvaluation(
            customerId = dto.customerId!!,
            totalCredit = dto.totalCredit!!
        )
    }
}

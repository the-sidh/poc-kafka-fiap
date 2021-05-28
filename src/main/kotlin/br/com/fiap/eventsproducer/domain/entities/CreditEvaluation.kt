package br.com.fiap.eventsproducer.domain.entities

import java.math.BigDecimal

data class CreditEvaluation(
        val customerId: String,
        val totalCredit: BigDecimal
)
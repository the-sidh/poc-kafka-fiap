package br.com.fiap.eventsproducer.domain.entities

import io.azam.ulidj.ULID
import java.math.BigDecimal

fun creditEvaluationSample() = CreditEvaluation(
        customerId = ULID.random(),
        totalCredit = BigDecimal(1000.00)
)
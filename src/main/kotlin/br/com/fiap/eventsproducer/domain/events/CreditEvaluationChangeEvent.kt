package br.com.fiap.eventsproducer.domain.events

import br.com.fiap.eventsproducer.domain.entities.CreditEvaluation
import io.azam.ulidj.ULID

data class CreditEvaluationChangeEvent(val eventId: String, val creditEvaluation: CreditEvaluation) {
    companion object {
        fun create(creditEvaluation: CreditEvaluation) = CreditEvaluationChangeEvent(
            eventId = ULID.random(),
            creditEvaluation = creditEvaluation)
    }
}

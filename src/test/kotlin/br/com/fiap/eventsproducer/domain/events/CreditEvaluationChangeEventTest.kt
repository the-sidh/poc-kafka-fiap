package br.com.fiap.eventsproducer.domain.events

import br.com.fiap.eventsproducer.domain.entities.creditEvaluationSample
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CreditEvaluationChangeEventTest (){

    @Test
    fun `given a credit evaluation when creating a credit evaluation event should return an object that represents a credit evaluation change event`(){
        val creditEvaluation = creditEvaluationSample()
        val event = CreditEvaluationChangeEvent.create(creditEvaluation)
        Assertions.assertEquals(creditEvaluation.customerId,event.creditEvaluation?.customerId)
        Assertions.assertEquals(creditEvaluation.totalCredit,event.creditEvaluation?.totalCredit)
    }
}
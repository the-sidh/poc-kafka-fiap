package br.com.fiap.eventsproducer.domain.services

import br.com.fiap.eventsproducer.domain.events.CreditEvaluationChangeEvent

interface CreditEvaluationEventProducer{
    fun send(event:CreditEvaluationChangeEvent)
}
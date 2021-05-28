package br.com.fiap.eventsproducer.application.rest.controllers

import br.com.fiap.eventsproducer.application.rest.dto.CreditEvaluationDTO
import br.com.fiap.eventsproducer.domain.events.CreditEvaluationChangeEvent
import br.com.fiap.eventsproducer.domain.services.CreditEvaluationEventProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateCreditEvaluationController(@Autowired @Qualifier("kafkaEventProducer") val eventProducer: CreditEvaluationEventProducer) {
    @PostMapping("/credit/evaluation")
    fun createCreditEvaluation(@RequestBody creditEvaluationDTO: CreditEvaluationDTO): ResponseEntity<CreditEvaluationChangeEvent> {
        val event = CreditEvaluationChangeEvent.create(creditEvaluation = CreditEvaluationDTO.toDomainObjet(creditEvaluationDTO))
        eventProducer.send(event)
        return ResponseEntity.status(HttpStatus.CREATED).body(event)
    }
}

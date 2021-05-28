package br.com.fiap.eventsproducer.application.rest.controllers

import br.com.fiap.eventsproducer.application.rest.dto.creditEvaluationDTOSampler
import br.com.fiap.eventsproducer.domain.entities.creditEvaluationSample
import br.com.fiap.eventsproducer.domain.events.CreditEvaluationChangeEvent
import br.com.fiap.eventsproducer.domain.services.CreditEvaluationEventProducer
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest
@ExtendWith(SpringExtension::class)
class CreateCreditEvaluationControllerTest {

    @MockkBean
    private lateinit var producer: CreditEvaluationEventProducer

    @Test
    fun `when receive a request to create a credit evaluation should return HTTP status 201 and an credit evaluation event`() {
        val controller = CreateCreditEvaluationController(producer)
        every { producer.send(any()) } just Runs
        val creditEvaluation = creditEvaluationDTOSampler()
        val response = controller.createCreditEvaluation(creditEvaluation)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
        Assertions.assertEquals(creditEvaluation, response.body?.creditEvaluation)
    }

    @Test
    fun `when receive a request to create a credit evaluation should create an event`() {
        val controller = CreateCreditEvaluationController(producer)
        every { producer.send(any()) } just Runs
        val creditEvaluation = creditEvaluationDTOSampler()
        controller.createCreditEvaluation(creditEvaluation)
        verify { producer.send(event = any()) }
    }
}

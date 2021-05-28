package br.com.fiap.eventsproducer.application.rest.dto

import br.com.fiap.eventsproducer.domain.entities.creditEvaluationSample
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CreditEvaluationDTOTest {

    @Test
    fun `given a DTO credit evaluation object with null customerID, should throw an exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            CreditEvaluationDTO(null, BigDecimal.ONE)
        }
    }

    @Test
    fun `given a DTO credit evaluation object with null totalCredit, should throw an exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            CreditEvaluationDTO("TEST", null)
        }
    }

    @Test
    fun `given a DTO credit evaluation object, should create a domain object`() {
        val dto = creditEvaluationDTOSampler()
        val createdObject = CreditEvaluationDTO.toDomainObjet(dto)
        Assertions.assertEquals(dto.customerId, createdObject.customerId)
        Assertions.assertEquals(dto.totalCredit, createdObject.totalCredit)
    }
}

fun creditEvaluationDTOSampler(): CreditEvaluationDTO {
    val creditEvaluation = creditEvaluationSample()
    return CreditEvaluationDTO(
        customerId = creditEvaluation.customerId,
        totalCredit = creditEvaluation.totalCredit
    )
}
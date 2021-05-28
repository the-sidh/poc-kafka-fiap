package br.com.fiap.eventsproducer

import br.com.fiap.eventsproducer.application.rest.dto.CreditEvaluationDTO
import br.com.fiap.eventsproducer.application.rest.dto.creditEvaluationDTOSampler
import br.com.fiap.eventsproducer.domain.events.CreditEvaluationChangeEvent
import com.fasterxml.jackson.databind.ObjectMapper
import io.azam.ulidj.ULID
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.awaitility.kotlin.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Duration


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@EmbeddedKafka(topics = ["events"])
@TestPropertySource(
    properties = [
        "spring.kafka.producer.bootstrap-servers=\${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap-servers=\${spring.embedded.kafka.brokers}"])
class EventProducingTests() {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Autowired
    private lateinit var restTestTemplate: TestRestTemplate

    private lateinit var consumer: Consumer<String, String>

    @BeforeEach
    fun setup() {
        val configs = KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker)
        consumer = DefaultKafkaConsumerFactory(configs, StringDeserializer(), StringDeserializer()).createConsumer()
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer)
        val freshRandom = ULID.random()
        mockkStatic(ULID::class)
        every { ULID.random() } returns freshRandom

    }

    @AfterEach
    fun shutDown() {
        consumer.close()
        unmockkStatic(ULID::class)
    }

    @Test
    fun `given a valid CreditEvaluationDTO when call POST on credit evaluation endpoint should return HTTP Status 201`() {
        val creditEval = creditEvaluationDTOSampler()
        val request = HttpEntity<CreditEvaluationDTO>(creditEval)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
        val response = restTestTemplate.exchange("/credit/evaluation", HttpMethod.POST, request, CreditEvaluationChangeEvent::class.java)
        Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)

    }

    @Test
    fun `given a valid CreditEvaluationDTO when call POST on credit evaluation endpoint should produce the correct event`() {
        val creditEval = creditEvaluationDTOSampler()
        val request = HttpEntity<CreditEvaluationDTO>(creditEval)
        val headers = HttpHeaders()
        val expectEventContent = getExpectedEventFromDTO(creditEval)

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
        restTestTemplate.exchange("/credit/evaluation", HttpMethod.POST, request, CreditEvaluationChangeEvent::class.java)
        val consumerRecord = KafkaTestUtils.getSingleRecord(consumer, "events")
                .value()
        await.atMost(Duration.ofSeconds(5L))
                .untilAsserted {
                    Assertions.assertEquals(expectEventContent, consumerRecord)
                }


    }

    private fun getExpectedEventFromDTO(creditEval: CreditEvaluationDTO) =
            objectMapper.writeValueAsString(CreditEvaluationChangeEvent.create(CreditEvaluationDTO.toDomainObjet(creditEval)))
}
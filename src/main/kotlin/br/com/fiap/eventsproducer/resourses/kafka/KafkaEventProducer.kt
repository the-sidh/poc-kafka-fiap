package br.com.fiap.eventsproducer.resourses.kafka

import br.com.fiap.eventsproducer.domain.events.CreditEvaluationChangeEvent
import br.com.fiap.eventsproducer.domain.services.CreditEvaluationEventProducer
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@Qualifier("kafkaProducer")
class KafkaEventProducer(
    val objectMapper: ObjectMapper,
    val template: KafkaTemplate<String, String>,
    @Value("\${spring.kafka.template.default-topic}") val topic: String
) : CreditEvaluationEventProducer {

    override fun send(event: CreditEvaluationChangeEvent) {
        val producerRecord = getProducerRecord(event)
        template.send(producerRecord).get()
    }

    private fun getProducerRecord(event: CreditEvaluationChangeEvent): ProducerRecord<String, String> {
        val serializedEvent = objectMapper.writeValueAsString(event)
        val eventTypeHeader =
            RecordHeader("eventType", objectMapper.writeValueAsBytes(CreditEvaluationChangeEvent::class.simpleName!!))
        val headers = listOf<Header>(eventTypeHeader)
        return ProducerRecord(
            topic,
            0,
            event.eventId,
            serializedEvent,
            headers
        )
    }
}
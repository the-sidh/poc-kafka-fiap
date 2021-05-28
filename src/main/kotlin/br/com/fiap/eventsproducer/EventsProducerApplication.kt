package br.com.fiap.eventsproducer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan()
class EventsProducerApplication

fun main(args: Array<String>) {
	runApplication<EventsProducerApplication>(*args)
}

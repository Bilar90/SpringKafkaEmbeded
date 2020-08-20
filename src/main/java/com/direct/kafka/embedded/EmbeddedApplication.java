package com.direct.kafka.embedded;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka
@EnableKafka
@SpringBootApplication
public class EmbeddedApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmbeddedApplication.class, args);
	}

	@Bean
	EmbeddedKafkaBroker kafkaBroker() {
		EmbeddedKafkaBroker broker = new EmbeddedKafkaBroker(1, true);
		broker.brokerProperty("listeners", "PLAINTEXT://localhost:9092");

		return broker;

	}

}

package com.direct.kafka.embedded;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RunWith(SpringRunner.class)
@EmbeddedKafka(partitions = 1, topics = { "testTopic" })
@SpringBootTest
public class EmbeddedApplicationTests {

	private static final String TEST_TOPIC = "testTopic";

	@Autowired
	EmbeddedKafkaBroker embeddedKafkaBroker;
	String str = new String();

	@Test
	public void testReceivingKafkaEvents() {
		Consumer<Integer, String> consumer = configureConsumer();
		Producer<Integer, String> producer = configureProducer();

		producer.send(new ProducerRecord<>(TEST_TOPIC, 123, "my-test-value"));

		ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);

		System.out.println("TEST EMBEDED");
		System.out.println(singleRecord.value());

		consumer.close();
		producer.close();
	}

	private Consumer<Integer, String> configureConsumer() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
				.createConsumer();
		consumer.subscribe(Collections.singleton(TEST_TOPIC));
		return consumer;
	}

	private Producer<Integer, String> configureProducer() {
		Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		return new DefaultKafkaProducerFactory<Integer, String>(producerProps).createProducer();
	}
}

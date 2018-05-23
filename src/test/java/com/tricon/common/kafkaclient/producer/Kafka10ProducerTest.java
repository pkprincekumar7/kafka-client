package com.tricon.common.kafkaclient.producer;

import java.util.UUID;

import org.junit.Test;

import com.tricon.common.KafkaClientBaseTest;



public class Kafka10ProducerTest extends KafkaClientBaseTest {

    @Test
    public void testSampleMessage() {
        Kafka10Producer producer = (Kafka10Producer) applicationContext.getBean("myTestProducerOne");
        Header header = new Header();
        header.setId(UUID.randomUUID().toString());
        header.setSource("customer-api");
        header.setType("JSON");
        header.setVersion("1.0");

        KafkaMessage message = new KafkaMessage("randomtopic", header, "Test message-3 latest msg");
        producer.send(message);

    }
}

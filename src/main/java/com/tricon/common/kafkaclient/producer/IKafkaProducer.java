package com.tricon.common.kafkaclient.producer;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.tricon.common.kafkaclient.eventstore.vo.IEventObject;

public interface IKafkaProducer {
	void close();
	void send(ProducerRecord<String, IEventObject> producerRecord);
	void send(KafkaMessage kafkaMessage);
}

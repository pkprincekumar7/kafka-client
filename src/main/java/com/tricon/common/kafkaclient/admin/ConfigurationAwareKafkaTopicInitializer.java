package com.tricon.common.kafkaclient.admin;

import java.util.List;

public class ConfigurationAwareKafkaTopicInitializer {
	
	private KafkaTopicInitializer kafkaTopicInitializer;
	
	//private BaseConfiguration baseConfiguration;

	public ConfigurationAwareKafkaTopicInitializer(List<KafkaTopicInitializer.KafkaTopic> topics, BaseConfiguration baseConfiguration) {
		
		if (baseConfiguration.isPropertyTrue("events.enabled")) {
			for (KafkaTopicInitializer.KafkaTopic topic : topics) {
				if (topic.getReplication() == 0) {
					topic.setReplication(baseConfiguration.getPropertyAsInt("default.replication"));
				}
			}

			kafkaTopicInitializer = new KafkaTopicInitializer(
					baseConfiguration.getProperty("environment.prefix"),
					topics, baseConfiguration
					);
		}
		
	}
}

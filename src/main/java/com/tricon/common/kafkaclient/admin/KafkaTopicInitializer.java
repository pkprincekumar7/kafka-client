package com.tricon.common.kafkaclient.admin;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

public class KafkaTopicInitializer {
	private Logger logger = LoggerFactory.getLogger(KafkaTopicInitializer.class);
	
	private BaseConfiguration baseConfiguration;

	private String topicPrefix;
	private String zookeeper;
	private List<KafkaTopic> topics;
	public KafkaTopicInitializer(String topicPrefix, List<KafkaTopic> topics, BaseConfiguration baseConfiguration) {
		this.topicPrefix = topicPrefix == null ? "" : topicPrefix;
		this.topics = topics;
		this.baseConfiguration = baseConfiguration;
		this.zookeeper = baseConfiguration.getPropertyRequired("zookeeper.host") + ":" + baseConfiguration.getPropertyRequired("zookeeper.port");
		if (isKafkaMessagingEnabled()) {
			logger.debug("Initiating topics");
			initTopics();
		}
	}
	private void initTopics() {
		int sessionTimeoutMs = 10000;
		int connectionTimeoutMs = 10000;
		ZkClient zkClient = new ZkClient(zookeeper, sessionTimeoutMs, connectionTimeoutMs,kafka.utils.ZKStringSerializer$.MODULE$);
		ZkConnection zkConnection = new ZkConnection(zookeeper, sessionTimeoutMs);
		ZkUtils zkUtils = new ZkUtils(zkClient, zkConnection, false);
		
		for (KafkaTopic topic : topics) {
			String fullTopicName = topicPrefix + topic.baseName;
			System.out.println("fullTopicName :" + fullTopicName);
			if (!AdminUtils.topicExists(zkUtils, fullTopicName)) {
				AdminUtils.createTopic(zkUtils, fullTopicName, topic.partitions, topic.replication, new Properties(), RackAwareMode.Safe$.MODULE$);
				logger.info("Topic {} was created, partitions:{}, replication:{}", fullTopicName,
						topic.partitions, topic.replication );
			} else {
				logger.info("Topic {} is already exists in Kafka", fullTopicName);
			}
		}
	}
	public static class KafkaTopic {
		private String baseName;
		private int replication = 1;
		private int partitions = 10;
		private Boolean highLoad;

		public KafkaTopic(String baseName, int replication, int partitions) {
			this.baseName = baseName;
			this.replication = replication;
			this.partitions = partitions;
		}

		public KafkaTopic(String baseName, int partitions) {
			this.baseName = baseName;
			this.partitions = partitions;
		}

		public KafkaTopic(String baseName, Boolean highLoad) {
			this.baseName = baseName;
			this.highLoad = highLoad;
		}

		public KafkaTopic(String baseName) {
			this.baseName = baseName;
		}


		public String getBaseName() {
			return baseName;
		}

		public int getReplication() {
			return replication;
		}

		public int getPartitions() {
			return partitions;
		}

		public Boolean getHighLoad() {
			return highLoad;
		}

		public KafkaTopic setReplication(int replication) {
			this.replication = replication;
			return this;
		}

		public KafkaTopic setPartitions(int partitions) {
			this.partitions = partitions;
			return this;
		}

		@Override
		public String toString() {
			return ReflectionToStringBuilder.toString(this);
		}
	}
	
	private boolean isKafkaMessagingEnabled(){
		return baseConfiguration.isPropertyTrue("events.enabled");
	}
}

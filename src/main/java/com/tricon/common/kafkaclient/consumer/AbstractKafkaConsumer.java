package com.tricon.common.kafkaclient.consumer;

import com.tricon.common.kafkaclient.producer.ZKService;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by Shukla, Sachin. on 6/29/17.
 */
public abstract class AbstractKafkaConsumer {

    private static final String DEFAULT_ZK_SESSION_TIMEOUT_MS = "500";
    private static final String DEFAULT_ZK_SYNC_TIME_MS = "250";
    private static final String DEFAULT_ZK_AUTO_COMMIT_INTERVAL_MS = "1000";

    private Map<String, String> map = new HashMap<String, String>();
    private ZKService zkService = null;
    protected ConsumerConnector consumer = null;
    private String topic = null;

    private List<KafkaStream<byte[], byte[]>> streams;

    public AbstractKafkaConsumer(ZKService zkService, Map<String, String> map){
        this.zkService = zkService;
        this.map = map;
    }

    protected abstract void run();

    protected List<KafkaStream<byte[], byte[]>> getStreams() {
        return streams;
    }

    public void init(){
        Properties props = new Properties();
        props.put("zookeeper.connect", zkService.getZooKeeperUrl());
        props.put("group.id", map.get("group.id") == null ? "group-id-0": map.get("group.id"));
        props.put("zookeeper.session.timeout.ms", map.get("zookeeper.session.timeout.ms") == null ? DEFAULT_ZK_SESSION_TIMEOUT_MS :map.get("zookeeper.session.timeout.ms") );
        props.put("zookeeper.sync.time.ms", map.get("zookeeper.sync.time.ms") == null ? DEFAULT_ZK_SYNC_TIME_MS : map.get("zookeeper.sync.time.ms"));
        props.put("auto.commit.interval.ms", map.get("auto.commit.interval.ms") == null ? DEFAULT_ZK_AUTO_COMMIT_INTERVAL_MS : map.get("auto.commit.interval.ms"));

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
        this.topic = map.get("topic.name");

        Map<String, Integer> topicCount = new HashMap<>();
        topicCount.put(topic, 1);

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount);
        this.streams = consumerStreams.get(topic);

        this.run();
    }

    public void destroy(){
        if (consumer != null) {
            consumer.shutdown();
        }
    }
    public Map<String, String> getPropertyMap(){
        return Collections.unmodifiableMap(map);
    }


}

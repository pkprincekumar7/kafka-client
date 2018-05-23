package com.tricon.common.kafkaclient.producer;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.tricon.common.kafkaclient.admin.BaseConfiguration;
import com.tricon.common.kafkaclient.configuration.KafkaConstants;
import com.tricon.common.kafkaclient.eventstore.vo.Event;
import com.tricon.common.kafkaclient.eventstore.vo.IEventObject;
import com.tricon.common.kafkaclient.exception.KafkaClientException;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Shukla, Sachin. on 6/24/17.
 * <p>
 * This is an abstract Kafka Producer. The Producer class should extend this and pass the required configuration
 * parameters e.g. zookeeprurl and other requried params.
 */


public class Kafka10Producer implements IKafkaProducer {
    private static Logger logger = LoggerFactory.getLogger(Kafka10Producer.class);

    private volatile boolean initialized = false;
    private Producer<String, String> producer = null;
    private ZKService zkService = null;
    @Autowired
    private BaseConfiguration baseConfiguration;
    private ProducerRecord<String, String> producerRecord;
    private boolean eventStoreEnabled = false;
    private Properties props = new Properties();

    public Kafka10Producer(ZKService zkService) {
        this.zkService = zkService;
    }

    private void print() {
        logger.info("zookeeper.URL " + zkService.getZooKeeperUrl());
        logger.info("acks " + baseConfiguration.getProperty("acks"));
        logger.info("retries " + baseConfiguration.getProperty("retries"));
        logger.info("batch.size " + baseConfiguration.getProperty("batch.size"));
        logger.info("linger.ms " + baseConfiguration.getProperty("linger.ms"));
        logger.info("buffer.memory " + baseConfiguration.getPropertyAsInt("buffer.memory"));
        logger.info("key.serializer " + baseConfiguration.getProperty("key.serializer"));
        logger.info("value.serializer " + baseConfiguration.getProperty("value.serializer"));
    }

    public void init() {
        this.print();
        if (initialized) {
            logger.warn("The producer is already initialized, hencing ignoring re-initialization.");
            return;
        }
        Preconditions.checkNotNull(baseConfiguration);

        if(baseConfiguration.isPropertyTrue("eventstore.enabled")){
            eventStoreEnabled = true;
            logger.info("Storing events in Event store is enabled.");

        }

        if (zkService.getZooKeeperUrl() == null) {
            throw new KafkaClientException("Property \"kafka.zookeeper\" is mandatory and cannot be skipped.");
        }

        props = new Properties();
        try {
            List<String> brokerList = zkService.getBrokersList();
            props.put(KafkaConstants.BOOTSTRAP_SERVERS, brokerList);
        } catch (Exception e1) {
            throw new KafkaClientException("Error while making a call to get the Broker list", e1);
        }
        Preconditions.checkNotNull(props.get(KafkaConstants.BOOTSTRAP_SERVERS));

        try {
            props.put(KafkaConstants.KEY_SERIALIZER, baseConfiguration.getProperty("key.serializer"));
            props.put(KafkaConstants.VALUE_SERIALIZER, baseConfiguration.getProperty("value.serializer"));
            props.put("acks", baseConfiguration.getProperty("acks"));
            props.put("retries", baseConfiguration.getPropertyAsInt("retries"));
            props.put("batch.size", baseConfiguration.getPropertyAsInt("batch.size"));
            props.put("buffer.memory", baseConfiguration.getPropertyAsInt("buffer.memory"));
            producer = new KafkaProducer<>(props);
            logger.info("KafkaProducer initialized. Brokers: {}" + props.get(KafkaConstants.BOOTSTRAP_SERVERS));
        } catch (Exception ex) {
            throw new KafkaClientException("Exception occurred while initializing the Kafka client", ex);
        }
        initialized = true;
    }

    public void close() {
        if (null != producer) {
            producer.close();
        }
    }


    public void send(KafkaMessage kafkaMessage) {
        if (!initialized) {
            this.init();
        }
        logger.debug("Attempt sending to Kafka ({}): {}, with key {}", kafkaMessage.getTopic(),
                kafkaMessage.getContent(), kafkaMessage.getKey());
        try {
            producerRecord = new ProducerRecord<String, String>(
                    kafkaMessage.getTopic(), kafkaMessage.getContent());
            if(eventStoreEnabled) {
                EventStoreUtil.saveEvent(getEvent(kafkaMessage));
            }
            producer.send(producerRecord);
            logger.debug("Sent to Kafka ({}): {}, with key {}", kafkaMessage.getTopic(),
                    kafkaMessage.getContent(), kafkaMessage.getKey());

        } catch (Exception e) {
            //In case when Brokers rotate the IP and for that exception new broker list needs to be retrieved
            //and should be re-tried //TODO
            logger.error("Error while sending to Kafka failed", e);
            throw new KafkaClientException(e).setParameter("topic", kafkaMessage.getTopic()).setParameter("content", kafkaMessage.getContent()).setParameter("key", kafkaMessage.getKey());
        } finally {
            //TODO : Need to check weather flush is working with the tricon Arch otherwise fall back to producer.close()
            producer.flush();
        }
    }
    public void send(ProducerRecord producerRecord) {
		if (!initialized) {
			this.init();
		}
		logger.debug("Attempt sending to Kafka ({}): {}, with key {}", producerRecord.key(),
				producerRecord.value().toString(), producerRecord.key());
		
		if (eventStoreEnabled) {
			EventStoreUtil.saveEvent(getEvent(producerRecord));
		}
		
		try {
			producer.send(producerRecord);
		} catch (Exception e) {
			// In case when Brokers rotate the IP and for that exception new
			// broker list needs to be retrieved
			// and should be re-tried //TODO
			
			logger.error("Error while sending to Kafka failed", e);
			
			//EventStoreUtil.saveEvent(exceptionByEvent(producerRecord,e));
			throw new KafkaClientException(e).setParameter("topic", producerRecord.key())
					.setParameter("content", producerRecord.value().toString()).setParameter("key", producerRecord.key());
		} finally {
			producer.flush();
		}

	}

    private Event getEvent(KafkaMessage kafkaMessage) {
        Event event = new Event();
        if(kafkaMessage.getHeader().getId() != null){
            event.setId(kafkaMessage.getHeader().getId());
        }else {
            event.setId(UUID.randomUUID().toString());
        }
        event.setSource(kafkaMessage.getHeader().getSource());
        event.setType(kafkaMessage.getHeader().getType());
        event.setFormatVersion(kafkaMessage.getHeader().getVersion());
        event.setCreatedDate(new Date());
        event.setData(kafkaMessage.getContent());
        return event;
    }
    private Event exceptionByEvent(ProducerRecord<String,IEventObject> producerRecord,Exception e) {
        Event event = getEvent(producerRecord);
        event.setData(event.getData()+e.getMessage());
        return event;
    }

	private Event getEvent(ProducerRecord<String,IEventObject> producerRecord) {
		Event event = new Event();
		event.setId(((IEventObject)producerRecord.value()).getHeader().getId());
		event.setSource(((IEventObject)producerRecord.value()).getHeader().getSource());
		event.setFormatVersion(((IEventObject)producerRecord.value()).getHeader().getVersion());
		event.setCreatedDate(new Date());
		event.setData(producerRecord.value().toString());
		event.setType(((IEventObject)producerRecord.value()).getHeader().getType());
		return event;
	}
    public ZKService getZkService() {
        return zkService;
    }

}
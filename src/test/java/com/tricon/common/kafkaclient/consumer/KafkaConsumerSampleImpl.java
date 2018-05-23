package com.tricon.common.kafkaclient.consumer;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tricon.common.kafkaclient.producer.ZKService;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * Created by Shukla, Sachin. on 6/29/17.
 */



public class KafkaConsumerSampleImpl extends AbstractKafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerSampleImpl.class);

    public KafkaConsumerSampleImpl(ZKService zkService, Map<String, String> map){
        super(zkService, map);
    }

    @Override
    protected void run() {

        logger.info("Inside KafkaConsumerSampleImpl#run()....");
        List<KafkaStream<byte[], byte[]>> streams =  getStreams();

        logger.info("streams.size = "+streams.size());

        for (final KafkaStream stream : streams) {
            logger.info("Inside stream : "+stream.clientId());
            ConsumerIterator<byte[], byte[]> it = stream.iterator();

            while (it.hasNext()) {
                logger.info("Message from Topic: " + new String(it.next().message()));
                break;
            }
            break;
        }
    }
}

package com.tricon.common.kafkaclient.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tricon.common.KafkaClientBaseTest;


public class KafkaConsumerTest extends KafkaClientBaseTest {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerTest.class);

//    @Test
    public void testSampleConsumer() {
        AbstractKafkaConsumer consumer = (AbstractKafkaConsumer) applicationContext.getBean("myTestConsumerOne");
    }  
} 
   
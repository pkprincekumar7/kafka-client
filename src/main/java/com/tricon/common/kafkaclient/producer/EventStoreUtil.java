package com.tricon.common.kafkaclient.producer;

import com.tricon.common.kafkaclient.eventstore.service.IEventStoreService;
import com.tricon.common.kafkaclient.eventstore.vo.Event;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

@Component
public class EventStoreUtil implements ApplicationContextAware{

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void saveEvent(Event event){
        applicationContext.getBean(IEventStoreService.class).save(event);
    }
}

package com.tricon.common.kafkaclient.eventstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tricon.common.kafkaclient.eventstore.dao.IEventStoreDao;
import com.tricon.common.kafkaclient.eventstore.vo.Event;
/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

@Service
public class EventStoreService implements IEventStoreService{

    @Autowired
    IEventStoreDao eventStoreDao;

    @Override
	@Transactional
    public void save(Event event) {
        eventStoreDao.save(event);
    }
}

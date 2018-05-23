package com.tricon.common.kafkaclient.eventstore.service;

import com.tricon.common.kafkaclient.eventstore.vo.Event;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */
public interface IEventStoreService {
    public void save(Event event);
}

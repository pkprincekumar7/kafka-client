package com.tricon.common.kafkaclient.eventstore.dao;

import com.tricon.common.kafkaclient.eventstore.vo.Event;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

public interface IEventStoreDao {
    public void save(Event event);
}

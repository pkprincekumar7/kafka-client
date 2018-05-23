package com.tricon.common.kafkaclient.eventstore.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.tricon.common.kafkaclient.eventstore.vo.Event;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

@Repository
public class EventStoreDao extends BaseDao implements IEventStoreDao {
	private static Logger logger = LoggerFactory.getLogger(EventStoreDao.class);

	private static final String SAVE_EVENT_SQL = "insert into event (id, source, event_type, format_version, event_data,createdAT)  "
			+ "  values (:id, :source, :eventType, :formatVersion, to_json(:eventData::text), now())";

	@Override
	public void save(Event event) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", event.getId());
		paramMap.put("source", event.getSource());
		paramMap.put("eventType", event.getType());
		paramMap.put("formatVersion", event.getFormatVersion());
		paramMap.put("eventData", event.getData());
		namedParameterJdbcTemplate.update(SAVE_EVENT_SQL, paramMap);
		logger.info("Event {" + event + "} saved successfully in Event Store database.");
	}
}

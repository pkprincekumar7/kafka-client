package com.tricon.common.kafkaclient.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shukla, Sachin. on 6/24/17.
 * This is a generic Runtime Exception thrown if there are any issues encounted in Kafka Client Module.
 */

public class KafkaClientException extends RuntimeException {

	private Map<String, Object> paramMap = new HashMap<String, Object>();

	public KafkaClientException() {
	}

	public KafkaClientException(String message) {
		super(message);
	}

	public KafkaClientException(Throwable cause) {
		super(cause);
	}

	public KafkaClientException(String message, Throwable cause) {
		super(message, cause);
	}
	public KafkaClientException setParameter(String key, Object value) {
		this.paramMap.put(key, value);
		return this;
	}

	
}

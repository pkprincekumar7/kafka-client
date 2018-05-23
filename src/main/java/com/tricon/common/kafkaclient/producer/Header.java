package com.tricon.common.kafkaclient.producer;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created by ragu on 01-07-2017.
 */
public class Header {
	 	String id;
	    String source;
	    Date timestamp;
	    String type;
	    String version;

	    public String getId() {
	        return id;
	    }

	    public Header setId(String id) {
	        this.id = id;
	        return this;
	    }

	    public String getSource() {
	        return source;
	    }

	    public Header setSource(String source) {
	        this.source = source;
	        return this;
	    }

	    public Date getTimestamp() {
	        return timestamp;
	    }

	    public Header setTimestamp(Date timestamp) {
	        this.timestamp = timestamp;
	        return this;
	    }

	    public String getType() {
	        return type;
	    }

	    public Header setType(String type) {
	        this.type = type;
	        return this;
	    }

	    public String getVersion() {
	        return version;
	    }

	    public Header setVersion(String version) {
	        this.version = version;
	        return this;
	    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}


package com.tricon.common.kafkaclient.eventstore.vo;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import sun.reflect.Reflection;

import java.util.Date;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

public class Event {
    private String id;
    private String source;
    private String type; //This should ve ENUM eventually //TODO
    private String formatVersion;
    private String data;
    private Date createdDate;


    @Override
    public String toString(){
        return ReflectionToStringBuilder.toString(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

package com.tricon.common.kafkaclient.producer;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class KafkaMessage {
    private String topic;
    private String content;
    private String key;
    private Header header;

    public KafkaMessage(String topic, Header header, String content) {
        this.topic = topic;
        this.header = header;
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

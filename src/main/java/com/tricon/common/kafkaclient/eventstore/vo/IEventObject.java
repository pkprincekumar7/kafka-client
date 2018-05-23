package com.tricon.common.kafkaclient.eventstore.vo;

import java.util.Map;

import com.tricon.common.kafkaclient.producer.Header;

public interface IEventObject  {



    public Header getHeader();

  

    public Object getData();

   
}

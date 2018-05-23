package com.tricon.common.kafkaclient.producer;

import org.apache.zookeeper.KeeperException;
import org.codehaus.jettison.json.JSONException;

import java.util.List;

/**
 * Created by ragu on 12-07-2017.
 */
public interface IZKService {

    void init();

    List<String> getBrokersList() throws KeeperException, InterruptedException, JSONException;

    String getZooKeeperUrl();
}

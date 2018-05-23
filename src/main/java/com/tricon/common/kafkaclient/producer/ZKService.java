package com.tricon.common.kafkaclient.producer;

import java.util.ArrayList;
import java.util.List;

import com.tricon.common.kafkaclient.admin.BaseConfiguration;
import com.tricon.common.kafkaclient.configuration.KafkaConstants;
import com.tricon.common.kafkaclient.exception.KafkaClientException;
import org.apache.zookeeper.KeeperException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Shukla, Sachin. on 6/24/17.
 *
 * This is ZooKeeper helper service. Used to get the latest Broker list
 */

public class ZKService implements IZKService{
	private static Logger logger = LoggerFactory.getLogger(ZKService.class);

	private ZooKeeper zooKeeper = null;
	private String zooKeeperUrl = null;

	@Autowired
	private BaseConfiguration baseConfiguration;

	public ZKService(){

	}

	@Override
	public void init(){
		this.zooKeeperUrl = baseConfiguration.getPropertyRequired("zookeeper.url");
		try {
			zooKeeper = new ZooKeeper(zooKeeperUrl, 60000, new Watcher() {
				public void process(WatchedEvent we) {
				}
			});
			logger.info("Inside ZKService#init()....Zookeeper service started successfully");
		}catch(Exception ex){
			throw new KafkaClientException("Exception while initiating ZKService.", ex);
		}

	}

	@Override
	public List<String> getBrokersList() throws KeeperException, InterruptedException, JSONException {

		List<String> brokerList = new ArrayList<String>();
		List<String> ids = zooKeeper.getChildren("/brokers/ids", false);

		for (String id : ids) {
			String brokerInfo = new String(zooKeeper.getData("/brokers/ids/" + id, false, null));
			JSONObject jsonObj = new JSONObject(brokerInfo);
			String host = jsonObj.getString(KafkaConstants.HOST);
			String port = jsonObj.getString(KafkaConstants.PORT);
			String brokeradd = host + ":" + port;
			brokerList.add(brokeradd);
		}
		logger.info("Inside ZKService#getBrokersList()....brokerList = "+brokerList);
		return brokerList;
	}

	@Override
	public String getZooKeeperUrl() {
		return zooKeeperUrl;
	}

}

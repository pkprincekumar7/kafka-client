package com.tricon.common.kafkaclient.admin;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class BaseConfiguration {
	private static Logger logger = LoggerFactory.getLogger(BaseConfiguration.class);
	private String env;
	private Map<String, String> specificConfigurations = Collections.emptyMap();

	public BaseConfiguration(String env){
		Preconditions.checkNotNull(env);
		this.env = env;
	}

	public BaseConfiguration(String env, Map<String, String> specificConfigurations){
		Preconditions.checkNotNull(env);
		this.env = env;
		this.specificConfigurations = specificConfigurations;
	}

	private Config kafkaConfig;
	private Config dbConfig;

	public void init(){
		kafkaConfig = ConfigFactory.load("config/"+env+"/application.conf").getConfig("kakfa");
		dbConfig = ConfigFactory.load("config/"+env+"/application.conf").getConfig("postgres");
	}

	public String getDBProperty(String key) {
		if(!specificConfigurations.isEmpty() && specificConfigurations.containsKey(key)){
			return specificConfigurations.get(key);
		}
		return this.dbConfig.getString(key);
	}

	public String getProperty(String key) {
		return this.getStringValue(key);
	}

	public String getPropertyRequired(String key) {
		String stringValue = getProperty(key);
		if (stringValue == null) {
			throw new IllegalArgumentException("Required property with key " + key + " requested but not found");
		}
		return stringValue;
	}
	
	private String getStringValue(String key) {
		if(!specificConfigurations.isEmpty() && specificConfigurations.containsKey(key)){
			return specificConfigurations.get(key);
		}
		return kafkaConfig.getString(key);
	}

	public boolean isPropertyTrue(String key) {
		if(!specificConfigurations.isEmpty() && specificConfigurations.containsKey(key)){
			return specificConfigurations.get(key).equalsIgnoreCase("true");
		}
		return kafkaConfig.getString(key).equalsIgnoreCase("true");
	}
	
	public int getPropertyAsInt(String key) {
		return this.getNumberValue(key);
	}
	
	private int getNumberValue(String key) {
		if(!specificConfigurations.isEmpty() && specificConfigurations.containsKey(key)){
			return Integer.parseInt(specificConfigurations.get(key));
		}
		return kafkaConfig.getInt(key);
	}

}

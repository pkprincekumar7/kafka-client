package com.tricon.common.kafkaclient.eventstore.dao;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Created by Shukla, Sachin. on 6/26/17.
 */

@Repository
public class BaseDao  extends JdbcDaoSupport {

    @Autowired
    @Qualifier("eventdataSource")
    protected DataSource eventdataSource;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

    @PostConstruct
    private void initialize() {
        setDataSource(eventdataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(eventdataSource);
    }
}

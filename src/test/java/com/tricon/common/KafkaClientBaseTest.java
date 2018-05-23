package com.tricon.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Shukla, Sachin. on 6/24/17.
 * This is the base test class and all other test class should directly or indirectly inherit this.
 * This would initialize sprint context and other needed dependencies which can be inherited by child classes.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:kafka-client-beans-test.xml"})

public class KafkaClientBaseTest {

    @Autowired
    protected ApplicationContext applicationContext;

    @Test
    public void testBase(){
        System.out.println("Running the test cases");

        for(String s : applicationContext.getBeanDefinitionNames()){
            System.out.println("Bean = "+s);
        }
    }
}

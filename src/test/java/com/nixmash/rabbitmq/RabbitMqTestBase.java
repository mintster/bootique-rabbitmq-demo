package com.nixmash.rabbitmq;


import com.nixmash.rabbitmq.ui.IRabbitMqUI;
import com.nixmash.rabbitmq.ui.RabbitMqUI;
import io.bootique.BQRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class RabbitMqTestBase {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqTestBase.class);

    protected static RabbitMqUI rabbitMqUI;

    @ClassRule
    public static BQTestFactory TEST_FACTORY = new BQTestFactory();

    @BeforeClass
    public static void setupDB() throws SQLException {
        BQRuntime runtime = TEST_FACTORY
                .app("--config=classpath:test.yml")
                .module(b -> b.bind(IRabbitMqUI.class).to(RabbitMqUI.class))
                .autoLoadModules()
                .createRuntime();

        rabbitMqUI = runtime.getInstance(RabbitMqUI.class);
    }

    @Test
    public void keyFixNotNullTest() {
        assertNotNull(rabbitMqUI);
    }


}

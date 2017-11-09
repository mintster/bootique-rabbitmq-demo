package com.nixmash.rabbitmq;


import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQRuntime;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import io.bootique.test.junit.BQTestFactory;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RabbitMqTestBase implements Module {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqTestBase.class);

    protected static ConnectionFactory connectionFactory;
    protected static ChannelFactory channelFactory;

    @ClassRule
    public static BQTestFactory TEST_FACTORY = new BQTestFactory();

    @BeforeClass
    public static void setupDB() throws SQLException {
        BQRuntime runtime = TEST_FACTORY
                .app("--config=classpath:test.yml")
                .module(RabbitMqTestBase.class)
                .autoLoadModules()
                .createRuntime();

        connectionFactory = runtime.getInstance(ConnectionFactory.class);
        channelFactory = runtime.getInstance(ChannelFactory.class);
    }

    @Test
    public void msgGoAway() {
        assertTrue(true);
    }


    @Override
    public void configure(Binder binder) {
    }
}

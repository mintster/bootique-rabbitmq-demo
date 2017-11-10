package com.nixmash.rabbitmq.recv;


import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.rabbitmq.common.service.ReservationService;
import com.nixmash.rabbitmq.common.service.ReservationServiceImpl;
import com.nixmash.rabbitmq.recv.ui.IProcessUI;
import com.nixmash.rabbitmq.recv.ui.ProcessUI;
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

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class ReceiverTestBase implements Module {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverTestBase.class);

    protected static ConnectionFactory connectionFactory;
    protected static ChannelFactory channelFactory;
    protected static ReservationService reservationService;

    @ClassRule
    public static BQTestFactory TEST_FACTORY = new BQTestFactory();

    @BeforeClass
    public static void setupDB() throws SQLException {
        BQRuntime runtime = TEST_FACTORY
                .app("--config=classpath:test.yml")
                .module(ReceiverTestBase.class)
                .autoLoadModules()
                .createRuntime();

        connectionFactory = runtime.getInstance(ConnectionFactory.class);
        channelFactory = runtime.getInstance(ChannelFactory.class);
        reservationService = runtime.getInstance(ReservationService.class);
    }

    @Test
    public void msgGoAway() {
        assertNotNull(connectionFactory);
    }


    @Override
    public void configure(Binder binder) {
        binder.bind(IProcessUI.class).to(ProcessUI.class);
        binder.bind(ReservationService.class).to(ReservationServiceImpl.class);
    }
}

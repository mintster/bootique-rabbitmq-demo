package com.nixmash.rabbitmq.send.ui;

import com.google.inject.Inject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SendUI implements ISendUI {

    private static final Logger logger = LoggerFactory.getLogger(SendUI.class);
    private static final String QUEUE_NAME = "bqDemo";
    private static final String CONNECTION_NAME = "bqDemoConnection";
    private static final String EXCHANGE_NAME = "bqDemo";

    private ConnectionFactory connectionFactory;
    private ChannelFactory channelFactory;

    @Inject
    public SendUI(ConnectionFactory connectionFactory, ChannelFactory channelFactory) {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
    }

    @Override
    public void init() throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(CONNECTION_NAME);
        Channel channel = channelFactory.openChannel(connection, EXCHANGE_NAME, QUEUE_NAME, "");
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }
}

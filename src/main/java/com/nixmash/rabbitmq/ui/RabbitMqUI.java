package com.nixmash.rabbitmq.ui;

import com.google.inject.Inject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUI implements IRabbitMqUI {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqUI.class);
    private static final String QUEUE_NAME = "bqQueue";

    private ConnectionFactory factory;

    @Inject
    public RabbitMqUI(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void init() throws IOException, TimeoutException {
        Connection connection = factory.forName("BQlocal");
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();

        logger.info("in RabbitMqUI...");
    }
}

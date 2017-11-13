package com.nixmash.rabbitmq.recv.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.*;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProcessUI implements IProcessUI {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUI.class);

    private static final String QUEUE_NAME = "bqDemo";
    private static final String CONNECTION_NAME = "bqDemoConnection";
    private static final String EXCHANGE_NAME = "bqDemo";

    private static final String UTF8 = "UTF-8";

    private ConnectionFactory connectionFactory;
    private CommonUI commonUI;

    @Inject
    public ProcessUI(ConnectionFactory connectionFactory, CommonUI commonUI) {
        this.connectionFactory = connectionFactory;
        this.commonUI = commonUI;
    }

    @Override
    public void handleMessageQueue() throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(CONNECTION_NAME);
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + " [at] " + commonUI.getDateTime() + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

    private static void doWork(String task) {
        System.out.println(" [x] Doing the work...");
    }
}

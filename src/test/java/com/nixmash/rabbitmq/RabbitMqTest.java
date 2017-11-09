package com.nixmash.rabbitmq;

import com.rabbitmq.client.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class RabbitMqTest extends RabbitMqTestBase {

    private static final String TEST_CONNECTION_NAME = "bqTestConnection";
    private static final String TEST_QUEUE_NAME = "bqTest";
    private static final String TEST_EXCHANGE_NAME = "bqTest";
    private static final String TEST_MESSAGE = "Hello Test World!";
    private static final String UTF8 = "UTF-8";

    @Test
    public void connectionFactoryTest() throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(TEST_CONNECTION_NAME);
        Channel channel = connection.createChannel();
//        channel.queueDeclare(TEST_QUEUE_NAME, true, true, false, null);
        channel.basicPublish(StringUtils.EMPTY, TEST_QUEUE_NAME, null, TEST_MESSAGE.getBytes(UTF8));

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, UTF8);
                assertTrue(message.equals(TEST_MESSAGE));
                channel.basicAck(envelope.getDeliveryTag(), true);
            }
        };
        channel.basicConsume(TEST_QUEUE_NAME, false, consumer);
        channel.close();
        connection.close();
    }

    @Test
    public void channelFactoryTest() throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(TEST_CONNECTION_NAME);
        Channel channel = channelFactory.openChannel(connection, TEST_EXCHANGE_NAME, TEST_QUEUE_NAME, "");
        channel.basicPublish("", TEST_QUEUE_NAME, null, TEST_MESSAGE.getBytes(UTF8));
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, UTF8);
                assertTrue(message.equals(TEST_MESSAGE));
                System.out.println(message);
                channel.basicAck(envelope.getDeliveryTag(), true);
            }
        };
        channel.basicConsume(TEST_QUEUE_NAME, true, "howdy", consumer);
        channel.close();
        connection.close();
    }
}

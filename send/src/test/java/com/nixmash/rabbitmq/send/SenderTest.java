package com.nixmash.rabbitmq.send;

import com.nixmash.rabbitmq.common.dto.Reservation;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SenderTest extends SenderTestBase {

    private static final String TEST_CONNECTION_NAME = "bqTestConnection";
    private static final String TEST_QUEUE_NAME = "bqTest";
    private static final String TEST_EXCHANGE_NAME = "bqTest";
    private static final String TEST_MESSAGE = "Hello Test World!";
    private static final String UTF8 = "UTF-8";

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
                assertNotNull(message);
                assertTrue(message.equals(TEST_MESSAGE));
                channel.basicAck(envelope.getDeliveryTag(), true);
            }
        };
        channel.basicConsume(TEST_QUEUE_NAME, true, "howdy", consumer);
        channel.close();
        connection.close();
    }

    @Test
    public void getReservationsTest() {
        List<Reservation> reservations = reservationService.getReservationList();
        assertTrue(reservations.size() > 3);
    }

}

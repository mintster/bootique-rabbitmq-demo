package com.nixmash.rabbitmq.send;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void jsonJacksonTest() throws IOException {
        String NAME = "Bammer";
        Reservation reservation = new Reservation(NAME);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(reservation);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, reservation);

        JSONReader jsonReader = new JSONReader();
        reservation = mapper.readValue(out.toString(), Reservation.class);
        assertTrue(reservation.getName().equals(NAME));
    }

    @Test
    public void jsonWriterTest() {
        JSONWriter writer = new JSONWriter(true);
        Reservation reservation = new Reservation("Bammer");
        String json = writer.write(reservation);
    }

    @Test
    public void bracketsContentTest() {
        String text = "{Bammer}";
        if (text.startsWith("{"))
            text = text.split("[\\{\\}]")[1];
        assertEquals(text, "Bammer");
    }
}

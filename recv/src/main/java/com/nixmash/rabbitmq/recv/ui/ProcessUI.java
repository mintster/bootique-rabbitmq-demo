package com.nixmash.rabbitmq.recv.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONReader;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;

public class ProcessUI implements IProcessUI {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUI.class);

    private ConnectionFactory connectionFactory;
    private CommonUI commonUI;

    @Inject
    public ProcessUI(ConnectionFactory connectionFactory, CommonUI commonUI) {
        this.connectionFactory = connectionFactory;
        this.commonUI = commonUI;
    }

    @Override
    public void handleMessageQueue() throws IOException {
        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received Message '" + message + " [at] " + commonUI.getDateTime() + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(MESSAGE_QUEUE, false, consumer);
    }

    @Override
    public void handleReservationQueue() throws IOException {
        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                JSONReader jsonReader = new JSONReader();
                ObjectMapper mapper = new ObjectMapper();
                Reservation reservation = mapper.readValue(body, Reservation.class);
                System.out.println(" [x] Received '" + reservation.toString() + " [at] " + commonUI.getDateTime() + "'");
                try {
                    doWork(reservation);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(RESERVATION_QUEUE, false, consumer);
    }

    private static void doWork(String task) {
        System.out.println(" [x] Processing the message...");
    }

    private static void doWork(Reservation reservation) {
        System.out.println(" [x] Processing the reservation with name: " + reservation.getName());
    }
}

package com.nixmash.rabbitmq.recv.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.nixmash.rabbitmq.common.service.ReservationService;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.*;
import com.rabbitmq.tools.json.JSONReader;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.nixmash.rabbitmq.common.ui.CommonUI.CONNECTION;
import static com.nixmash.rabbitmq.common.ui.CommonUI.RPC_MESSAGE_QUEUE;

public class RpcProcessUI implements IRpcProcessUI {

    private static final Logger logger = LoggerFactory.getLogger(ProcessUI.class);

    private ConnectionFactory connectionFactory;
    private CommonUI commonUI;
    private ReservationService reservationService;

    @Inject
    public RpcProcessUI(ConnectionFactory connectionFactory, CommonUI commonUI, ReservationService reservationService) {
        this.connectionFactory = connectionFactory;
        this.commonUI = commonUI;
        this.reservationService = reservationService;
    }

    @Override
    public void handleRpcMessageQueue() throws IOException {
        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = connection.createChannel();
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();

                String response = "";
                try {
                    JSONReader jsonReader = new JSONReader();
                    ObjectMapper mapper = new ObjectMapper();
                    Reservation reservation = mapper.readValue(body, Reservation.class);
                    System.out.println(" [x] Received '" + reservation.toString() + " [at] " + commonUI.getDateTime() + "'");
                    // TODO: Add reservation processing, return reservation
                    response += reservationService.getPastVisitMessage(reservation.getName());
                } catch (RuntimeException e) {
                    System.out.println(" [.] EXCEPTION:  " + e.toString());
                } finally {
                    channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    // RabbitMq consumer worker thread notifies the RPC server owner thread
                    synchronized (this) {
                        this.notify();
                    }
                }
            }
        };
        channel.basicConsume(RPC_MESSAGE_QUEUE, false, consumer);
        while (true) {
            synchronized (consumer) {
                try {
                    consumer.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

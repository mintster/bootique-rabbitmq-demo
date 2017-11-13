package com.nixmash.rabbitmq.send.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;

public class SendUI implements ISendUI {

    private static final Logger logger = LoggerFactory.getLogger(SendUI.class);

    private ConnectionFactory connectionFactory;
    private ChannelFactory channelFactory;
    private CommonUI commonUI;

    @Inject
    public SendUI(ConnectionFactory connectionFactory, ChannelFactory channelFactory, CommonUI commonUI) {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
        this.commonUI = commonUI;
    }

    @Override
    public void cmdLineSend() throws IOException, TimeoutException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Boolean sending = true;
        while (sending) {
            System.out.print("Enter a message or a reservation name in {brackets}. [ENTER] to quit: ");
            String message = br.readLine();
            if (!message.equals(StringUtils.EMPTY)) {
                if (message.startsWith("{")) {
                    Reservation reservation = new Reservation(message.split("[\\{\\}]")[1]);
                    sendReservation(reservation);
                } else
                    sendMessage(message);
            } else
                sending = false;
        }
    }

    @Override
    public void staticSendMessage() throws IOException, TimeoutException {
        sendMessage("Hello World!");
    }

    @Override
    public void staticSendReservation() throws IOException, TimeoutException {
        sendReservation(new Reservation("John"));
    }

    private void sendMessage(String message) throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(CONNECTION_NAME);
        Channel channel = channelFactory.openChannel(connection, MESSAGE_EXCHANGE_NAME, MESSAGE_QUEUE_NAME, "");
        message = message + " : " + commonUI.getDateTime();
        channel.basicPublish("", MESSAGE_QUEUE_NAME, null, message.getBytes(UTF8));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

    private void sendReservation(Reservation reservation) throws IOException, TimeoutException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(reservation);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, reservation);

        Connection connection = connectionFactory.forName(CONNECTION_NAME);
        Channel channel = channelFactory.openChannel(connection, RESERVATION_EXCHANGE_NAME, RESERVATION_QUEUE_NAME, "");
        channel.basicPublish("", RESERVATION_QUEUE_NAME, null, out.toByteArray());
        System.out.println(" [x] Sent '" + json + "'");
        channel.close();
        connection.close();
    }
}

package com.nixmash.rabbitmq.send.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class SendUI implements ISendUI {

    private static final Logger logger = LoggerFactory.getLogger(SendUI.class);
    private static final String QUEUE_NAME = "bqDemo";
    private static final String CONNECTION_NAME = "bqDemoConnection";
    private static final String EXCHANGE_NAME = "bqDemo";

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
            System.out.print("Enter a message. [ENTER] to quit: ");
            String message = br.readLine();
            if (!message.equals(StringUtils.EMPTY) && !message.toLowerCase().equals("exit")) {
                sendMessage(message);
            }
            else
                sending = false;
        }
    }

    @Override
    public void staticSend() throws IOException, TimeoutException {
        sendMessage("Hello World!");
    }

    private void sendMessage(String message) throws IOException, TimeoutException {
        Connection connection = connectionFactory.forName(CONNECTION_NAME);
        Channel channel = channelFactory.openChannel(connection, EXCHANGE_NAME, QUEUE_NAME, "");
        message = message + " : " + commonUI.getDateTime();
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }

}

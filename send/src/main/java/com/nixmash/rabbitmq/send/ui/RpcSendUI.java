package com.nixmash.rabbitmq.send.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.*;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;

public class RpcSendUI implements IRpcSendUI {

    private static final Logger logger = LoggerFactory.getLogger(RpcSendUI.class);
    private ConnectionFactory connectionFactory;
    private ChannelFactory channelFactory;
    private CommonUI commonUI;

    private Channel channel;
    private Connection connection;

    @Inject
    public RpcSendUI(ConnectionFactory connectionFactory, ChannelFactory channelFactory, CommonUI commonUI) throws IOException, TimeoutException {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
        this.commonUI = commonUI;
        this.connection = connectionFactory.forName(CONNECTION);
        this.channel = channelFactory.openChannel(connection, RPC_MESSAGE_EXCHANGE, RPC_MESSAGE_QUEUE, "");
    }

    @Override
    public void sendRpcMessage() {
        String response = null;
        System.out.println(" [x] Requesting fib(30)");
        try {
            response = call("30");
            System.out.println(" [.] Got '" + response + "'");
/*            channel.close();
            connection.close();*/
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    public String call(String message) throws IOException, InterruptedException, TimeoutException {
        String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", RPC_MESSAGE_QUEUE, props, message.getBytes("UTF-8"));
        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });
        return response.take();
    }
}

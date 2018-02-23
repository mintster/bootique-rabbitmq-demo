package com.nixmash.rabbitmq.recv.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.rabbitmq.client.*;
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

    @Inject
    public RpcProcessUI(ConnectionFactory connectionFactory, CommonUI commonUI) {
        this.connectionFactory = connectionFactory;
        this.commonUI = commonUI;
    }

    @Override
    public void handleRpcMessageQueue() throws IOException {
        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = connection.createChannel();
        channel.basicQos(1);
//        System.out.println(" [.] Awaiting RPC requests");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();

                String response = "";
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println(" [.] Received Message : '" + message + " [at] " + commonUI.getDateTime() + "'");
                    int n = Integer.parseInt(message);
                    response += fib(n);
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

    private static int fib(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;
        return fib(n - 1) + fib(n - 2);
    }
}

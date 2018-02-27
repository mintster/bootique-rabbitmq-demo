package com.nixmash.rabbitmq.send.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.dto.Customer;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.rabbitmq.client.*;
import io.bootique.rabbitmq.client.channel.ChannelFactory;
import io.bootique.rabbitmq.client.connection.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;

public class RpcSendUI implements IRpcSendUI {

    private static final Logger logger = LoggerFactory.getLogger(RpcSendUI.class);
    private ConnectionFactory connectionFactory;
    private ChannelFactory channelFactory;


    @Inject
    public RpcSendUI(ConnectionFactory connectionFactory, ChannelFactory channelFactory) throws IOException, TimeoutException {
        this.connectionFactory = connectionFactory;
        this.channelFactory = channelFactory;
    }

    @Override
    public void cmdLineRpcSend() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("GUEST NAME RETURNS A CUSTOMER OBJECT. SENDING A RESERVATION OBJECT RETURNS INFO MESSAGE.");
        System.out.println("----------------------------------------------------------------------------\n");
        Boolean sending = true;
        while (sending) {
            System.out.print("Enter guest name for Customer Data Record, in {brackets} to retrieve check-in message: ");
            String message = null;
            try {
                message = br.readLine();
            } catch (IOException e) {
                System.out.println("Exception on data input: " + e.getMessage());
                System.exit(-1);
            }
            if (!message.equals(StringUtils.EMPTY)) {
                try {
                    if (message.equals("?")) {
                        sendRpcListRequest();
                    }
                    else if (message.startsWith("{")) {
                        Reservation reservation = new Reservation(message.split("[\\{\\}]")[1]);
                        sendRpcReservation(reservation);
                    } else
                        sendRpcMessage(message);
                } catch (Exception e) {
                    System.out.println("Exception sending to queue: " + e.getMessage());
                    System.exit(-1);
                }
            } else
                sending = false;
        }
    }

    private void sendRpcMessage(String message) {
        Customer response = null;
        try {
            response = call(message);
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /*
  Sending Guest Name will return Customer POJO
   */
    private Customer call(String name) throws IOException, InterruptedException, TimeoutException {
        String corrId = UUID.randomUUID().toString();

        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = channelFactory.openChannel(connection,
                RPC_MESSAGE_EXCHANGE, RPC_MESSAGE_QUEUE, "");
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", RPC_MESSAGE_QUEUE, props, name.getBytes(UTF8));
        final BlockingQueue<Customer> response = new ArrayBlockingQueue<Customer>(1);

        channel.basicConsume(replyQueueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    ObjectMapper mapper = new ObjectMapper();
                    Customer customer= mapper.readValue(body, Customer.class);
                    response.offer(customer);
                }
            }
        });
        Customer take = response.take();
        channel.close();
        connection.close();
        return take;
    }

    private void sendRpcListRequest() {
        List<Customer> response = null;
        try {
            response = call();
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /*
  Sending Request for Customer POJO List
   */
    private List<Customer> call() throws IOException, InterruptedException, TimeoutException {
        String corrId = UUID.randomUUID().toString();

        //region Setup Connection and Queue
        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = channelFactory.openChannel(connection,
                RPC_LIST_EXCHANGE, RPC_LIST_QUEUE, "");
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        //endregion

        channel.basicPublish("", RPC_LIST_QUEUE, props, null);
        final BlockingQueue<List<Customer>> response = new ArrayBlockingQueue<>(1);

        channel.basicConsume(replyQueueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<Customer> customers = mapper.readValue(body,
                            TypeFactory
                                    .defaultInstance()
                                    .constructCollectionType(List.class, Customer.class));
                    response.offer(customers);
                }
            }
        });
        List<Customer> take = response.take();
        channel.close();
        connection.close();
        return take;
    }

    private void sendRpcReservation(Reservation reservation) {
        String response = null;
        try {
            response = call(reservation);
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    /*
    SENDING A RESERVATION OBJECT. WILL RETURN CHECK-IN MESSAGE FROM SERVER
     */
    private String call(Reservation reservation) throws IOException, InterruptedException, TimeoutException {
        String corrId = UUID.randomUUID().toString();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(reservation);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        mapper.writeValue(out, reservation);

        Connection connection = connectionFactory.forName(CONNECTION);
        Channel channel = channelFactory.openChannel(connection,
                RPC_RESERVATION_EXCHANGE, RPC_RESERVATION_QUEUE, "");
        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", RPC_RESERVATION_QUEUE, props, out.toByteArray());
        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });
        String take = response.take();
        channel.close();
        connection.close();
        return take;
    }
}

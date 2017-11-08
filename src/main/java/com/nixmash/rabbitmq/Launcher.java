package com.nixmash.rabbitmq;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.rabbitmq.ui.IRabbitMqUI;
import com.nixmash.rabbitmq.ui.RabbitMqUI;
import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.rabbitmq.client.connection.ConnectionConfig;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Launcher implements Module {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Launcher.class);

    private ConnectionConfig connectionConfig;
    private RabbitMqUI rabbitMqUI;

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
//                .args("-H")
                .args("--config=classpath:bootique.yml")
                .module(Launcher.class)
                .autoLoadModules().createRuntime();

//        runtime.run();
        try {
            runtime.getInstance(RabbitMqUI.class).init();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(IRabbitMqUI.class).to(RabbitMqUI.class);
    }
}

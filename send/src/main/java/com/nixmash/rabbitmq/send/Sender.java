package com.nixmash.rabbitmq.send;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.rabbitmq.send.ui.ISendUI;
import com.nixmash.rabbitmq.send.ui.SendUI;
import io.bootique.BQRuntime;
import io.bootique.Bootique;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Sender implements Module {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Sender.class);

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Sender.class)
                .autoLoadModules().createRuntime();
        try {
            runtime.getInstance(SendUI.class).cmdLineSend();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ISendUI.class).to(SendUI.class);
    }
}

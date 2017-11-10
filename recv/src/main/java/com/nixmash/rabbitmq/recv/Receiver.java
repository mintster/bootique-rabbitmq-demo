package com.nixmash.rabbitmq.recv;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.rabbitmq.recv.ui.IProcessUI;
import com.nixmash.rabbitmq.recv.ui.ProcessUI;
import io.bootique.BQRuntime;
import io.bootique.Bootique;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receiver implements Module {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Receiver.class);

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Receiver.class)
                .autoLoadModules().createRuntime();
        try {
            runtime.getInstance(ProcessUI.class).handleMessageQueue();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(IProcessUI.class).to(ProcessUI.class);
    }
}

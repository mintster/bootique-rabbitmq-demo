package com.nixmash.rabbitmq.recv;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.nixmash.rabbitmq.common.config.RabbitConfig;
import com.nixmash.rabbitmq.common.service.ReservationService;
import com.nixmash.rabbitmq.common.service.ReservationServiceImpl;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.nixmash.rabbitmq.common.ui.ICommonUI;
import com.nixmash.rabbitmq.recv.ui.IProcessUI;
import com.nixmash.rabbitmq.recv.ui.IRpcProcessUI;
import com.nixmash.rabbitmq.recv.ui.ProcessUI;
import com.nixmash.rabbitmq.recv.ui.RpcProcessUI;
import io.bootique.BQRuntime;
import io.bootique.Bootique;

import java.io.IOException;

public class Receiver implements Module {

    @Inject
    private static RabbitConfig rabbitConfig;

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Receiver.class);

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Receiver.class)
                .autoLoadModules().createRuntime();

        try {
            CommonUI commonUI = runtime.getInstance(CommonUI.class);
            switch (commonUI.getAppStartup()) {
                case RPC:
                    runtime.getInstance(RpcProcessUI.class).handleRpcMessageQueue();
                    runtime.getInstance(RpcProcessUI.class).handleRpcReservationQueue();
                    break;
                case MESSAGES:
                    runtime.getInstance(ProcessUI.class).handleMessageQueue();
                    runtime.getInstance(ProcessUI.class).handleReservationQueue();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(IProcessUI.class).to(ProcessUI.class);
        binder.bind(ReservationService.class).to(ReservationServiceImpl.class);
        binder.bind(IRpcProcessUI.class).to(RpcProcessUI.class);
        binder.bind(ICommonUI.class).to(CommonUI.class);
    }
}

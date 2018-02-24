package com.nixmash.rabbitmq.send;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import com.nixmash.rabbitmq.common.ui.ICommonUI;
import com.nixmash.rabbitmq.send.ui.IRpcSendUI;
import com.nixmash.rabbitmq.send.ui.ISendUI;
import com.nixmash.rabbitmq.send.ui.RpcSendUI;
import com.nixmash.rabbitmq.send.ui.SendUI;
import io.bootique.BQRuntime;
import io.bootique.Bootique;

public class Sender implements Module {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Sender.class);

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Sender.class)
                .autoLoadModules().createRuntime();

        CommonUI commonUI = runtime.getInstance(CommonUI.class);
        switch (commonUI.getAppStartup()) {
            case MESSAGES:
                runtime.getInstance(SendUI.class).cmdLineSend();
                break;
            case RPC:
                runtime.getInstance(RpcSendUI.class).cmdLineRpcSend();
                break;
        }

    }

    @Override
    public void configure(Binder binder) {
        binder.bind(ISendUI.class).to(SendUI.class);
        binder.bind(IRpcSendUI.class).to(RpcSendUI.class);
        binder.bind(ICommonUI.class).to(CommonUI.class);
    }

}

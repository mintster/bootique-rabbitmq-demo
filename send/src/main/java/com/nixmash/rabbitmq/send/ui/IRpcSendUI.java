package com.nixmash.rabbitmq.send.ui;

import java.util.concurrent.TimeoutException;

public interface IRpcSendUI {
    void sendRpcMessage() throws TimeoutException;
}

package com.nixmash.rabbitmq.send.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ISendUI {
    void cmdLineSend() throws IOException, TimeoutException;

    void staticSend() throws IOException, TimeoutException;
}

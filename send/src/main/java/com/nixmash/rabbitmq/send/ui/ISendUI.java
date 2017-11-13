package com.nixmash.rabbitmq.send.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ISendUI {
    void cmdLineSend() throws IOException, TimeoutException;

    void staticSendMessage() throws IOException, TimeoutException;

    void staticSendReservation() throws IOException, TimeoutException;
}

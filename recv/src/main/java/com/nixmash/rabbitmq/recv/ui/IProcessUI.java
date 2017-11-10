package com.nixmash.rabbitmq.recv.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IProcessUI {
    void handleMessageQueue() throws IOException, TimeoutException;
}

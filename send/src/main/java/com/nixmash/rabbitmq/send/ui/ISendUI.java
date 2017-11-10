package com.nixmash.rabbitmq.send.ui;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ISendUI {
    void init() throws IOException, TimeoutException;
}

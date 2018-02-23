package com.nixmash.rabbitmq.recv.ui;

import java.io.IOException;

public interface IRpcProcessUI {
    void handleRpcMessageQueue() throws IOException;
}

package com.nixmash.rabbitmq.recv.ui;

import java.io.IOException;

public interface IRpcProcessUI {
    void handleRpcMessageQueue() throws IOException;

    void handleRpcListRequestQueue() throws IOException;

    void handleRpcReservationQueue() throws IOException;
}

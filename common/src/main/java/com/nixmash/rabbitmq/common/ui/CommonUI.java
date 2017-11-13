package com.nixmash.rabbitmq.common.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUI implements ICommonUI{

    public static final String MESSAGE_QUEUE_NAME = "bqMessages";
    public static final String RESERVATION_QUEUE_NAME = "bqReservations";
    public static final String CONNECTION_NAME = "bqConnection";
    public static final String MESSAGE_EXCHANGE_NAME = "bqMessages";
    public static final String RESERVATION_EXCHANGE_NAME = "bqReservations";
    public static final String UTF8 = "UTF-8";

    @Override
    public String getDateTime() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("M-dd-yyyy hh:mm:ss:SSS a z");
        return format.format(curDate);
    }
}

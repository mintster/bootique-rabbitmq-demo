package com.nixmash.rabbitmq.common.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUI implements ICommonUI{

    public static final String MESSAGE_QUEUE = "bqMessages";
    public static final String RESERVATION_QUEUE = "bqReservations";
    public static final String CONNECTION = "bqConnection";
    public static final String MESSAGE_EXCHANGE = "bqMessages";
    public static final String RESERVATION_EXCHANGE = "bqReservations";
    public static final String UTF8 = "UTF-8";

    @Override
    public String getDateTime() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("M-dd-yyyy hh:mm:ss:SSS a z");
        return format.format(curDate);
    }
}

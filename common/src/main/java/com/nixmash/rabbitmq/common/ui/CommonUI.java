package com.nixmash.rabbitmq.common.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.config.RabbitConfig;
import com.nixmash.rabbitmq.common.enums.AppStartup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUI implements ICommonUI{

    public static final String MESSAGE_QUEUE = "bqMessages";
    public static final String RPC_MESSAGE_QUEUE = "rpcMessages";
    public static final String RESERVATION_QUEUE = "bqReservations";
    public static final String CONNECTION = "bqConnection";
    public static final String MESSAGE_EXCHANGE = "bqMessages";
    public static final String RPC_MESSAGE_EXCHANGE = "rpcMessages";
    public static final String RESERVATION_EXCHANGE = "bqReservations";
    public static final String UTF8 = "UTF-8";

    private RabbitConfig rabbitConfig;

    @Inject
    public CommonUI(RabbitConfig rabbitConfig) {
        this.rabbitConfig = rabbitConfig;
    }

    @Override
    public String getDateTime() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("M-dd-yyyy hh:mm:ss:SSS a z");
        return format.format(curDate);
    }

    @Override
    public AppStartup getAppStartup() {
        return AppStartup.valueOf(rabbitConfig.startupType.toUpperCase());
    }
}

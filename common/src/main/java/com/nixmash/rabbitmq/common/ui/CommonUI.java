package com.nixmash.rabbitmq.common.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUI implements ICommonUI{

    @Override
    public String getDateTime() {
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("M-dd-yyyy hh:mm:ss:SSS a z");
        return format.format(curDate);
    }
}

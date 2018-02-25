package com.nixmash.rabbitmq.common.ui;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.config.RabbitConfig;
import com.nixmash.rabbitmq.common.dto.Reservation;
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


    public static final String BOB = "Bob";
    public static final String BILL = "Bill";
    public static final String JANET = "Janet";

    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";
    public static final String GENDER_NA = "NA";


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
    public String getPrettyCurrentVisit(Integer pastVisitCount) {
        Integer currentVisitCount = pastVisitCount + 1;
        return currentVisitCount + suffixes[currentVisitCount];
    }

    @Override
    public String getGenderHisHer(Reservation reservation) {
        String hisHer = GENDER_NA;
        if (reservation != null) {
            String gender = reservation.getGender();
            hisHer = gender.equals(GENDER_MALE) ? "his" : "her";
        }
        return hisHer;
    }

    @Override
    public AppStartup getAppStartup() {
        return AppStartup.valueOf(rabbitConfig.startupType.toUpperCase());
    }

    // from a stackoverflow answer...
    private String[] suffixes = {
        //    0     1     2     3     4     5     6     7     8     9
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                //    10    11    12    13    14    15    16    17    18    19
                "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                //    20    21    22    23    24    25    26    27    28    29
                "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                //    30    31
                "th", "st"
    } ;
}

package com.nixmash.rabbitmq.common.ui;

import com.nixmash.rabbitmq.common.dto.Reservation;
import com.nixmash.rabbitmq.common.enums.AppStartup;

public interface ICommonUI {
    String getDateTime();

    // from a stackoverflow answer...
    String getPrettyCurrentVisit(Integer pastVisitCount);

    String getGenderHisHer(Reservation reservation);

    AppStartup getAppStartup();
}

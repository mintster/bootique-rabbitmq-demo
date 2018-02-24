package com.nixmash.rabbitmq.common.ui;

import com.nixmash.rabbitmq.common.enums.AppStartup;

public interface ICommonUI {
    String getDateTime();

    AppStartup getAppStartup();
}

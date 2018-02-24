package com.nixmash.rabbitmq.common.config;


import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Singleton
public class RabbitConfig implements Serializable {

    private static final long serialVersionUID = -5505730332375176898L;
    private static final Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    // region properties

    public String startupType;

    // endregion

    // region get()

    public RabbitConfig() {

        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/common.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        this.startupType = properties.getProperty("startup.type");

    }

    // endregion

}
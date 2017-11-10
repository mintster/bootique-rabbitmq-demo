package com.nixmash.rabbitmq.common;

import com.google.inject.Binder;
import com.google.inject.Module;
import io.bootique.BQRuntime;
import io.bootique.Bootique;

public class Launcher implements Module {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        BQRuntime runtime = Bootique
                .app(args)
                .args("--config=classpath:bootique.yml")
                .module(Launcher.class)
                .autoLoadModules().createRuntime();
    }

    @Override
    public void configure(Binder binder) {
    }

}

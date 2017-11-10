package com.nixmash.rabbitmq.common.dto;

import java.io.Serializable;
import java.util.UUID;

public class Reservation implements Serializable {

    private static final long serialVersionUID = -3950680494791449573L;

    //region properties

    private UUID id;
    private String name;

    //endregion

    // region Constructor

    public Reservation(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
    }


    // endregion

    //region getter setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //endregion


    //region toString()
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
    //endregion
}

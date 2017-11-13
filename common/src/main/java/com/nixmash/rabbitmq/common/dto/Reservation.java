package com.nixmash.rabbitmq.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation implements Serializable {

    private static final long serialVersionUID = -3950680494791449573L;

    public Reservation() {
    }
    //region properties

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID id;
    private String name;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss:SSS")
    private Timestamp createdDateTime;

    //endregion

    // region Constructor

    public Reservation(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.createdDateTime = Timestamp.valueOf(LocalDateTime.now());
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

    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    //endregion


    //region toString()

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdDateTime=" + createdDateTime +
                '}';
    }


    //endregion
}

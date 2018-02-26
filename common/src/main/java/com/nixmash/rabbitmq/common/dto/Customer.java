package com.nixmash.rabbitmq.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Customer implements Serializable {
    private static final long serialVersionUID = -5717670638868210789L;

    private Long currentDateTime = new Date().getTime();
    public Customer() {
    }

    public Customer(String name, String gender, Integer visits) {
        this.name = name;
        this.gender = gender;
        this.visits = visits;
    }

    //region private properties

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private UUID id = UUID.randomUUID();
    private String name;
    private String gender = "NA";
    private Integer visits = 0;

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss:SSS")
    private Timestamp createdDateTime = new Timestamp(currentDateTime);

    public Customer(String guestName) {
        this.name = guestName;
    }
    //endregion


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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Timestamp getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Timestamp createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
    //endregion


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", visits=" + visits +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}

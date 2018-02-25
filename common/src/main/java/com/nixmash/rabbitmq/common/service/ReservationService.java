package com.nixmash.rabbitmq.common.service;

import com.google.inject.ImplementedBy;
import com.nixmash.rabbitmq.common.dto.Customer;
import com.nixmash.rabbitmq.common.dto.Reservation;

import java.util.List;

@ImplementedBy(ReservationServiceImpl.class)
public interface ReservationService {
    Reservation addReservation(String name);

    Reservation getReservation(String name);

    List<Reservation> getReservationList();

    String getPastVisitMessage(String guestName);

    Integer getPastVisitCount(String guestName);

    List<Customer> getCustomerList();
}

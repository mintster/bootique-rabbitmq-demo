package com.nixmash.rabbitmq.common.service;

import com.google.inject.ImplementedBy;
import com.nixmash.rabbitmq.common.dto.Reservation;

import java.util.List;

@ImplementedBy(ReservationServiceImpl.class)
public interface ReservationService {
    Reservation addReservation(String name);
    List<Reservation> getReservationList();
}

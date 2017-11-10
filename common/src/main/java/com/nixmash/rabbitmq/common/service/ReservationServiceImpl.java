package com.nixmash.rabbitmq.common.service;

import com.nixmash.rabbitmq.common.dto.Reservation;

import java.util.*;

public class ReservationServiceImpl implements ReservationService {

    @Override
    public Reservation addReservation(String name) {
        return new Reservation(name);
    }

    @Override
    public List<Reservation> getReservationList() {
        Set<String> names = new HashSet<>(Arrays.asList("Graham", "Simon", "Sussman", "Woodward", "Bernstein"));
        List<Reservation> reservations = new ArrayList<Reservation>();
        for (String name : names) {
            reservations.add(new Reservation(name));
        }
        return reservations;
    }
}

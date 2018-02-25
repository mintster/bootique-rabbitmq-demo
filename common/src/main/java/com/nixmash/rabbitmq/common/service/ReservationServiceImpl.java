package com.nixmash.rabbitmq.common.service;

import com.google.inject.Inject;
import com.nixmash.rabbitmq.common.dto.Customer;
import com.nixmash.rabbitmq.common.dto.Reservation;
import com.nixmash.rabbitmq.common.ui.CommonUI;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;

public class ReservationServiceImpl implements ReservationService {

    private CommonUI commonUI;

    @Inject
    public ReservationServiceImpl(CommonUI commonUI) {
        this.commonUI = commonUI;
    }

    // region Reservations

    @Override
    public Reservation addReservation(String name) {
        return new Reservation(name);
    }

    @Override
    public Reservation getReservation(String name) {
        Optional<Reservation> reservation;
        reservation = getReservationList()
                .stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst();
        return reservation.orElse(null);
    }

    @Override
    public List<Reservation> getReservationList() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation(BOB, GENDER_MALE));
        reservations.add(new Reservation(BOB, GENDER_MALE));
        reservations.add(new Reservation(BILL, GENDER_MALE));
        reservations.add(new Reservation(JANET, GENDER_FEMALE));
        reservations.add(new Reservation(JANET, GENDER_FEMALE));
        reservations.add(new Reservation(JANET, GENDER_FEMALE));
        return reservations;
    }

    @Override
    public String getPastVisitMessage(String guestName) {
        String pastVisitMessage = null;
        Integer pastVisitCount = getPastVisitCount(guestName);
        if (pastVisitCount > 0) {
            Reservation reservation = getReservation(guestName);
            pastVisitMessage = MessageFormat.format("{0} has been a guest with us {1} times before. This is {2} {3} visit.",
                    StringUtils.capitalize(guestName),
                    pastVisitCount,
                    commonUI.getGenderHisHer(reservation),
                    commonUI.getPrettyCurrentVisit(pastVisitCount));
        } else {
            pastVisitMessage = MessageFormat.format("{0} is a new guest!", StringUtils.capitalize(guestName));
        }
        return pastVisitMessage;
    }

    @Override
    public Integer getPastVisitCount(String guestName) {
        List<Reservation> reservations = getReservationList();
        return (int) reservations.stream().filter(r -> r.getName().equalsIgnoreCase(guestName)).count();
    }

// endregion

    // region Customers

    @Override
    public List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Bob", GENDER_MALE, 2));
        customers.add(new Customer("Bill", GENDER_MALE, 1));
        customers.add(new Customer("Janet", GENDER_FEMALE, 3));
        return customers;
    }

    // endregion

}

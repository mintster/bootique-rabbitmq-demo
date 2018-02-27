package com.nixmash.rabbitmq.common.service;

import com.nixmash.rabbitmq.common.CommonTestBase;
import com.nixmash.rabbitmq.common.dto.Customer;
import com.nixmash.rabbitmq.common.dto.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Optional;

import static com.nixmash.rabbitmq.common.ui.CommonUI.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ReservationServiceImplTest extends CommonTestBase {

    private static final String BOB = "bob";
    private static final String KEN = "Ken";

    @Test
    public void addReservationTest() {
        Reservation reservation = reservationService.addReservation(BOB);
        assertNotNull(reservation.getId());
        assertTrue(reservation.getName().equals(BOB));
    }

    @Test
    public void getReservationList() throws Exception {
        List<Reservation> reservationList = reservationService.getReservationList();
        assertTrue(reservationList.size() > 1);
    }

    @Test
    public void getReservationTest() throws Exception {
        Optional<Reservation> reservation;
        reservation = reservationService.getReservationList()
                .stream()
                .filter(r -> r.getName().equalsIgnoreCase(KEN))
                .findFirst();

        assertFalse(reservation.isPresent());
    }

    @Test
    public void getGenderHisHerTest() {
        Reservation noreservation = reservationService.getReservation("Ken");
        assertNull(noreservation);

        assertEquals(commonUI.getGenderHisHer(null), GENDER_NA);

        Reservation bob = reservationService.getReservation(BOB);
        String male = bob.getGender();
        assertEquals(male, GENDER_MALE);
        assertEquals(commonUI.getGenderHisHer(bob), "his");

        Reservation janet = reservationService.getReservation(JANET);
        String female = janet.getGender();
        assertEquals(female, GENDER_FEMALE);
        assertEquals(commonUI.getGenderHisHer(janet), "her");

    }

    @Test
    public void getPastVisitMessageTest() {
        assertTrue(reservationService.getPastVisitMessage(BOB).contains("This is his"));
        assertTrue(reservationService.getPastVisitMessage(JANET).contains("This is her"));
        assertTrue(reservationService.getPastVisitMessage(KEN).contains("new guest!"));
    }

    @Test
    public void getCustomerListTest() {
        List<Customer> customers = reservationService.getCustomerList();
       assertTrue(customers.size() > 1);
    }
}
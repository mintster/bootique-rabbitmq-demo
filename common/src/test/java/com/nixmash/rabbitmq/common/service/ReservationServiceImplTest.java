package com.nixmash.rabbitmq.common.service;

import com.nixmash.rabbitmq.common.CommonTestBase;
import com.nixmash.rabbitmq.common.dto.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReservationServiceImplTest extends CommonTestBase {

    private static final String BOB = "bob";

    @Test
    public void addReservationTest() {
        Reservation reservation = reservationServiceImpl.addReservation(BOB);
        assertNotNull(reservation.getId());
        assertTrue(reservation.getName().equals(BOB));
    }

    @Test
    public void getReservationList() throws Exception {
        List<Reservation> reservationList = reservationServiceImpl.getReservationList();
        assertTrue(reservationList.size() > 1);
    }

}
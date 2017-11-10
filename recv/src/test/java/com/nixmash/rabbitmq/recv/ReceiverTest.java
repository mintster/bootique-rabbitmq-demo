package com.nixmash.rabbitmq.recv;

import com.nixmash.rabbitmq.common.dto.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReceiverTest extends ReceiverTestBase {

    @Test
    public void getReservationsTest() {
        List<Reservation> reservations = reservationService.getReservationList();
        assertTrue(reservations.size() > 3);
    }

}

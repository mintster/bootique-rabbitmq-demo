package com.nixmash.rabbitmq.recv;

import com.nixmash.rabbitmq.common.dto.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ReceiverServiceImplTest extends ReceiverTestBase {

    @Test
    public void getCommonReservationsTest() {
        List<Reservation> reservations = reservationService.getReservationList();
        assertTrue(reservations.size() > 1);
    }

    @Test
    public void getCommonDateTimeTest() {
        assertNotNull(commonUI.getDateTime());
    }

    @Test
    public void reservationServicePastVisitsTest() {
        // Ignore case on guest name lookup
        assertTrue(reservationService.getPastVisitCount("bob") > 0);
        assertEquals(reservationService.getPastVisitCount("bob"), reservationService.getPastVisitCount("BoB"));
    }

}

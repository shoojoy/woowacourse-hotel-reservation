package hotel.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    private Reservation createReservation(LocalDate checkIn, LocalDate checkOut) {
        return new Reservation(
                1L,            // reservationId
                1L,            // hotelId
                1L,            // roomId
                "테스트예약자",
                "010-0000-0000",
                checkIn,
                checkOut
        );
    }

    @Test
    @DisplayName("예약 기간이 서로 겹치면 isOverlapped는 true를 반환한다")
    void overlappedDatesReturnTrue() {
        Reservation reservation = createReservation(
                LocalDate.of(2025, 12, 24),
                LocalDate.of(2025, 12, 26)
        );

        boolean result1 = reservation.isOverlapped(
                LocalDate.of(2025, 12, 24),
                LocalDate.of(2025, 12, 25)
        );

        boolean result2 = reservation.isOverlapped(
                LocalDate.of(2025, 12, 23),
                LocalDate.of(2025, 12, 25)
        );

        boolean result3 = reservation.isOverlapped(
                LocalDate.of(2025, 12, 24),
                LocalDate.of(2025, 12, 26)
        );

        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
    }

    @Test
    @DisplayName("체크아웃과 다른 예약의 체크인이 딱 붙어 있을 때는 겹치지 않는다")
    void backToBackDatesDoNotOverlap() {
        Reservation reservation = createReservation(
                LocalDate.of(2025, 12, 24),
                LocalDate.of(2025, 12, 26)
        );

        boolean resultBefore = reservation.isOverlapped(
                LocalDate.of(2025, 12, 22),
                LocalDate.of(2025, 12, 24)
        );

        boolean resultAfter = reservation.isOverlapped(
                LocalDate.of(2025, 12, 26),
                LocalDate.of(2025, 12, 28)
        );

        assertFalse(resultBefore);
        assertFalse(resultAfter);
    }
}

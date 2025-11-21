package hotel.storage;

import hotel.domain.Reservation;

import java.time.LocalDate;
import java.util.*;

public class InMemoryReservationStorage {

    private final Map<Long, Reservation> reservations = new HashMap<>();
    private long sequence = 1L;   // 예약 id 자동 증가 용도

    public Reservation save(Reservation reservationWithoutId) {
        Long newId = sequence++;

        Reservation saved = new Reservation(
                newId,
                reservationWithoutId.getHotelId(),
                reservationWithoutId.getRoomId(),
                reservationWithoutId.getGuestName(),
                reservationWithoutId.getPhoneNumber(),
                reservationWithoutId.getCheckInDate(),
                reservationWithoutId.getCheckOutDate()
        );

        reservations.put(newId, saved);
        return saved;
    }

    public Optional<Reservation> findById(Long reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    public List<Reservation> findByRoomId(Long roomId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation reservation : reservations.values()) {
            if (reservation.getRoomId().equals(roomId)) {
                result.add(reservation);
            }
        }
        return result;
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public void deleteById(Long reservationId) {
        reservations.remove(reservationId);
    }

    public void clear() {
        reservations.clear();
        sequence = 1L;
    }
}

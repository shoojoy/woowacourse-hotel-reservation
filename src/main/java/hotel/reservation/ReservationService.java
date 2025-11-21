package hotel.reservation;

import hotel.domain.Reservation;
import hotel.domain.Room;
import hotel.storage.InMemoryReservationStorage;
import hotel.storage.InMemoryRoomStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final InMemoryRoomStorage roomStorage;
    private final InMemoryReservationStorage reservationStorage;

    public ReservationService(InMemoryRoomStorage roomStorage,
                              InMemoryReservationStorage reservationStorage) {
        this.roomStorage = roomStorage;
        this.reservationStorage = reservationStorage;
    }

    public List<Room> findAvailableRooms(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int guestCount
    ) {
        validateDateRange(checkInDate, checkOutDate);
        if (guestCount <= 0) {
            throw new IllegalArgumentException("인원 수는 1명 이상이어야 합니다.");
        }

        // 1. 해당 호텔의 전체 객실 조회
        List<Room> allRooms = roomStorage.findByHotelId(hotelId);

        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            // 2. 인원 수용이 가능한 객실인지 확인
            if (room.getMaxGuestCount() < guestCount) {
                continue;
            }

            // 3. 이 객실에 이미 같은 기간에 예약이 있는지 확인
            boolean overlapped = reservationStorage.findByRoomId(room.getRoomId())
                    .stream()
                    .anyMatch(reservation -> reservation.isOverlapped(checkInDate, checkOutDate));

            if (!overlapped) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public Reservation reserve(
            Long hotelId,
            Long roomId,
            String guestName,
            String phoneNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        validateDateRange(checkInDate, checkOutDate);

        Room room = roomStorage.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 객실입니다. roomId=" + roomId));

        if (!room.getHotelId().equals(hotelId)) {
            throw new IllegalArgumentException("선택한 객실이 해당 호텔에 속해 있지 않습니다.");
        }

        boolean overlapped = reservationStorage.findByRoomId(roomId)
                .stream()
                .anyMatch(reservation -> reservation.isOverlapped(checkInDate, checkOutDate));

        if (overlapped) {
            throw new IllegalStateException("해당 기간에 이미 예약이 존재하는 객실입니다.");
        }

        Reservation newReservation = new Reservation(
                0L,
                hotelId,
                roomId,
                guestName,
                phoneNumber,
                checkInDate,
                checkOutDate
        );

        return reservationStorage.save(newReservation);
    }

    public void cancel(Long reservationId) {
        reservationStorage.deleteById(reservationId);
    }

    private void validateDateRange(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("체크인/체크아웃 날짜를 모두 입력해 주세요.");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜보다 뒤여야 합니다.");
        }
    }
}

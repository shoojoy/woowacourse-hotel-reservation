package hotel.reservation;

import hotel.domain.Reservation;
import hotel.domain.Room;
import hotel.domain.RoomGrade;
import hotel.storage.InMemoryReservationStorage;
import hotel.storage.InMemoryRoomStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private InMemoryRoomStorage roomStorage;
    private InMemoryReservationStorage reservationStorage;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        roomStorage = new InMemoryRoomStorage();
        reservationStorage = new InMemoryReservationStorage();
        reservationService = new ReservationService(roomStorage, reservationStorage);

        // 호텔 1번에 방 두 개 세팅
        roomStorage.save(new Room(
                1L,
                1L,  // hotelId
                "101",
                RoomGrade.STANDARD,
                2,
                80_000
        ));
        roomStorage.save(new Room(
                2L,
                1L,
                "102",
                RoomGrade.DELUXE,
                2,
                100_000
        ));
    }

    @Test
    @DisplayName("예약이 없으면 모든 객실이 예약 가능 객실로 조회된다")
    void findAvailableRooms_whenNoReservations_returnsAllRooms() {
        LocalDate checkIn = LocalDate.of(2025, 12, 24);
        LocalDate checkOut = LocalDate.of(2025, 12, 25);

        List<Room> availableRooms = reservationService.findAvailableRooms(
                1L,
                checkIn,
                checkOut,
                2
        );

        assertEquals(2, availableRooms.size());
    }

    @Test
    @DisplayName("최대 수용 인원보다 많은 인원을 조회하면 객실은 예약 가능 목록에 포함되지 않는다")
    void findAvailableRooms_excludesRoomsOverGuestCount() {
        LocalDate checkIn = LocalDate.of(2025, 12, 24);
        LocalDate checkOut = LocalDate.of(2025, 12, 25);

        List<Room> availableRooms = reservationService.findAvailableRooms(
                1L,
                checkIn,
                checkOut,
                3
        );

        assertTrue(availableRooms.isEmpty());
    }

    @Test
    @DisplayName("겹치는 예약이 없으면 예약을 생성할 수 있다")
    void reserve_success_whenNoOverlap() {
        LocalDate checkIn = LocalDate.of(2025, 12, 24);
        LocalDate checkOut = LocalDate.of(2025, 12, 25);

        Reservation reservation = reservationService.reserve(
                1L,
                1L,
                "새예약자",
                "010-1111-2222",
                checkIn,
                checkOut
        );

        assertNotNull(reservation.getReservationId());
        assertEquals(1L, reservation.getHotelId());
        assertEquals(1L, reservation.getRoomId());
        assertEquals("새예약자", reservation.getGuestName());

        // 실제로 저장되었는지도 한 번 더 확인
        List<Reservation> reservationsForRoom = reservationStorage.findByRoomId(1L);
        assertEquals(1, reservationsForRoom.size());
    }

    @Test
    @DisplayName("같은 방, 겹치는 날짜에 두 번째 예약을 시도하면 예외가 발생한다")
    void reserve_throwsException_whenOverlapped() {
        LocalDate checkIn = LocalDate.of(2025, 12, 24);
        LocalDate checkOut = LocalDate.of(2025, 12, 25);

        // 첫 예약
        reservationService.reserve(
                1L,
                1L,
                "첫예약자",
                "010-1111-2222",
                checkIn,
                checkOut
        );

        assertThrows(IllegalStateException.class, () ->
                reservationService.reserve(
                        1L,
                        1L,
                        "두번째예약자",
                        "010-3333-4444",
                        checkIn,
                        checkOut
                )
        );
    }
}

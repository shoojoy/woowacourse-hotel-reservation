package hotel;

import hotel.domain.Hotel;
import hotel.domain.Reservation;
import hotel.domain.Room;
import hotel.domain.RoomGrade;
import hotel.reservation.ReservationService;
import hotel.storage.InMemoryHotelStorage;
import hotel.storage.InMemoryReservationStorage;
import hotel.storage.InMemoryRoomStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // 인메모리 저장소 초기화
        InMemoryHotelStorage hotelStorage = new InMemoryHotelStorage();
        InMemoryRoomStorage roomStorage = new InMemoryRoomStorage();
        InMemoryReservationStorage reservationStorage = new InMemoryReservationStorage();

        // 초기 데이터(시더) 세팅
        seedHotels(hotelStorage);
        seedRooms(roomStorage);

        // 서비스 생성
        ReservationService reservationService =
                new ReservationService(roomStorage, reservationStorage);

        Long selectedHotelId = 1L; // 우아한 호텔 서울
        LocalDate checkIn = LocalDate.of(2025, 12, 24);
        LocalDate checkOut = LocalDate.of(2025, 12, 25);
        int guestCount = 2;

        System.out.println("=== 호텔 목록 ===");
        for (Hotel hotel : hotelStorage.findAll()) {
            System.out.printf("- [%d] %s (%s)%n",
                    hotel.getHotelId(),
                    hotel.getHotelName(),
                    hotel.getAddress());
        }

        System.out.println();
        System.out.printf("%d번 호텔에서 %s ~ %s, %d명 기준 예약 가능한 객실을 조회합니다.%n",
                selectedHotelId, checkIn, checkOut, guestCount);

        List<Room> availableRooms = reservationService.findAvailableRooms(
                selectedHotelId,
                checkIn,
                checkOut,
                guestCount
        );

        if (availableRooms.isEmpty()) {
            System.out.println("예약 가능한 객실이 없습니다.");
            return;
        }

        System.out.println("예약 가능한 객실:");
        for (Room room : availableRooms) {
            System.out.printf("- roomId=%d, 호수=%s, 등급=%s, 1박 가격=%d원%n",
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomGrade(),
                    room.getPricePerNight());
        }

        Room selectedRoom = availableRooms.get(0);
        System.out.println();
        System.out.printf("'%s' 객실을 선택했습니다.%n", selectedRoom.getRoomNumber());

        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.print("예약자 이름을 입력해 주세요: ");
        String guestName = scanner.nextLine();

        System.out.print("예약자 전화번호를 입력해 주세요: ");
        String phoneNumber = scanner.nextLine();

        Reservation reservation = reservationService.reserve(
                selectedHotelId,
                selectedRoom.getRoomId(),
                guestName,
                phoneNumber,
                checkIn,
                checkOut
        );

        System.out.println();
        System.out.printf("예약이 완료되었습니다. 예약 번호: %d, 예약자: %s%n",
                reservation.getReservationId(),
                reservation.getGuestName());

        System.out.println();
        System.out.println("호텔 예약 데모 시나리오를 종료합니다.");
    }

    private static void seedHotels(InMemoryHotelStorage hotelStorage) {
        hotelStorage.save(new Hotel(
                1L,
                "우아한 호텔 서울",
                "서울특별시 송파구 위례성대로 2"
        ));
        hotelStorage.save(new Hotel(
                2L,
                "우아한 호텔 부산",
                "부산광역시 해운대구 센텀서로 30"
        ));
        hotelStorage.save(new Hotel(
                3L,
                "우아한 호텔 경주",
                "경상북도 경주시 보문로 424-6"
        ));
    }

    private static void seedRooms(InMemoryRoomStorage roomStorage) {
        roomStorage.save(new Room(
                1L,
                1L,
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
        roomStorage.save(new Room(
                3L,
                1L,
                "103",
                RoomGrade.SUITE,
                2,
                120_000
        ));

        roomStorage.save(new Room(
                4L,
                2L,
                "201",
                RoomGrade.STANDARD,
                2,
                80_000
        ));
        roomStorage.save(new Room(
                5L,
                2L,
                "202",
                RoomGrade.DELUXE,
                2,
                100_000
        ));

        roomStorage.save(new Room(
                6L,
                3L,
                "301",
                RoomGrade.STANDARD,
                2,
                80_000
        ));
    }
}

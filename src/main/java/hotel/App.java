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
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        InMemoryHotelStorage hotelStorage = new InMemoryHotelStorage();
        InMemoryRoomStorage roomStorage = new InMemoryRoomStorage();
        InMemoryReservationStorage reservationStorage = new InMemoryReservationStorage();

        seedHotels(hotelStorage);
        seedRooms(roomStorage);

        ReservationService reservationService =
                new ReservationService(roomStorage, reservationStorage);

        Scanner scanner = new Scanner(System.in);

        System.out.println("==== 우아한 호텔 예약 시스템 ====");

        Long selectedHotelId = selectHotel(scanner, hotelStorage);
        if (selectedHotelId == null) {
            System.out.println("프로그램을 종료합니다.");
            return;
        }

        LocalDate checkIn = readDate(scanner, "체크인 날짜를 입력하세요. (예: 2025-12-24): ");
        LocalDate checkOut = readDate(scanner, "체크아웃 날짜를 입력하세요 (예:2025-12-25): ");

        int guestCount = readInt(scanner, "투숙 인원 수를 입력하세요.: ");

        List<Room> availableRooms = reservationService.findAvailableRooms(
                selectedHotelId,
                checkIn,
                checkOut,
                guestCount
        );

        if (availableRooms.isEmpty()) {
            System.out.println("해당 조건에 예약 가능한 객실이 없습니다.");
            System.out.println("프로그램을 종료합니다.");
            return;
        }

        System.out.println();
        System.out.println("예약 가능한 객실 목록: ");
        for (Room room : availableRooms) {
            System.out.printf("- roomId=%d, 호수=%s, 등급=%s, 1박 가격=%d원%n",
                    room.getRoomId(),
                    room.getRoomNumber(),
                    room.getRoomGrade(),
                    room.getPricePerNight());
        }

        Long selectedRoomId = readLong(scanner, "예약할 객실의 roomId를 입력하세요: ");

        Optional<Room> selectedRoomOpt = availableRooms.stream()
                .filter(r -> r.getRoomId().equals(selectedRoomId))
                .findFirst();

        if (selectedRoomOpt.isEmpty()) {
            System.out.println("예약 가능한 목록에 없는 객실입니다. 프로그램을 종료합니다.");
            return;
        }

        Room selectedRoom = selectedRoomOpt.get();
        System.out.printf("'%s' 객실을 선택하셨습니다.%n", selectedRoom.getRoomNumber());

        System.out.println();
        System.out.print("예약자 이름을 입력해 주세요: ");
        String guestName = scanner.nextLine().trim();

        System.out.print("예약자 전화번호를 입력해 주세요: ");
        String phoneNumber = scanner.nextLine().trim();

        try {
            Reservation reservation = reservationService.reserve(
                    selectedHotelId,
                    selectedRoom.getRoomId(),
                    guestName,
                    phoneNumber,
                    checkIn,
                    checkOut
            );

            System.out.println();
            System.out.println("예약이 완료되었습니다.");
            System.out.printf("예약 번호: %d%n", reservation.getReservationId());
            System.out.printf("호텔 ID: %d, 객실 호수: %s%n",
                    reservation.getHotelId(),
                    selectedRoom.getRoomNumber());
            System.out.printf("체크인: %s, 체크아웃:%s%n",
                    reservation.getCheckInDate(),
                    reservation.getCheckOutDate());
            System.out.printf("예약자: %s (%s)%n", reservation.getGuestName(), reservation.getPhoneNumber());
        } catch (IllegalStateException e) {
            System.out.println("해당 기간에 이미 예약이 존재하는 객실입니다. 다른 날짜나 객실을 선택해 주세요.");
        }

        System.out.println();
        System.out.println("우아한 호텔 예약 시스템을 종료합니다.");
    }

    private static Long selectHotel(Scanner scanner, InMemoryHotelStorage hotelStorage) {
        System.out.println();
        System.out.println("=== 호텔 목록 ===");
        for (Hotel hotel : hotelStorage.findAll()) {
            System.out.printf("- [%d] %s (%s)%n",
                    hotel.getHotelId(),
                    hotel.getHotelName(),
                    hotel.getAddress());
        }
        System.out.println("0을 입력하면 종료합니다.");

        while (true) {
            Long hotelId = readLong(scanner, "예약할 호텔 번호를 입력하세요: ");

            if (hotelId == 0L) {
                return null;
            }

            boolean exists = hotelStorage.findById(hotelId).isPresent();
            if (exists) {
                return hotelId;
            }

            System.out.println("존재하지 않는 호텔입니다. 다시 입력해 주세요.");
        }
    }

    private static LocalDate readDate(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("날짜 형식이 올바르지 않습니다. 예: 2025-12-24");
            }
        }
    }

    private static int readInt(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value <= 0) {
                    System.out.println("1 이상 숫자를 입력해 주세요.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
            }
        }
    }

    private static Long readLong(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
            }
        }
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

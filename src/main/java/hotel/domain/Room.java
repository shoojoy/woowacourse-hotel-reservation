package hotel.domain;

/**
 * 호텔의 객실(방) 한 개를 표현하는 도메인 모델입니다.
 */
public class Room {

    private final Long roomId;         // 객실 고유 번호
    private final Long hotelId;        // 어느 호텔에 속한 방인지
    private final String roomNumber;   // 호수 (예: "101")
    private final RoomGrade roomGrade; // STANDARD, DELUXE, SUITE ...
    private final int maxGuestCount;   // 최대 인원 수
    private final int pricePerNight;   // 1박 기준 가격 (원)

    public Room(Long roomId,
                Long hotelId,
                String roomNumber,
                RoomGrade roomGrade,
                int maxGuestCount,
                int pricePerNight) {

        if (roomId == null) {
            throw new IllegalArgumentException("roomId는 필수 값입니다.");
        }
        if (hotelId == null) {
            throw new IllegalArgumentException("hotelId는 필수 값입니다.");
        }
        if (roomNumber == null || roomNumber.isBlank()) {
            throw new IllegalArgumentException("객실 호수를 입력해 주세요.");
        }
        if (roomGrade == null) {
            throw new IllegalArgumentException("객실 등급을 선택해 주세요.");
        }
        if (maxGuestCount <= 0) {
            throw new IllegalArgumentException("최대 인원 수는 1명 이상이어야 합니다.");
        }
        if (pricePerNight < 0) {
            throw new IllegalArgumentException("객실 가격은 0원 이상이어야 합니다.");
        }

        this.roomId = roomId;
        this.hotelId = hotelId;
        this.roomNumber = roomNumber;
        this.roomGrade = roomGrade;
        this.maxGuestCount = maxGuestCount;
        this.pricePerNight = pricePerNight;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomGrade getRoomGrade() {
        return roomGrade;
    }

    public int getMaxGuestCount() {
        return maxGuestCount;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }
}

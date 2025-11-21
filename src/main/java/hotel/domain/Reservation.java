package hotel.domain;

import java.time.LocalDate;

public class Reservation {

    private final Long reservationId; // 예약 고유 번호
    private final Long hotelId; // 어느 호텔의 예약인지
    private final Long roomId; // 어느 객실의 예약인지

    // 예약자 정보
    private final String guestName;
    private final String phoneNumber;

    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;

    public Reservation(Long reservationId,
                       Long hotelId,
                       Long roomId,
                       String guestName,
                       String phoneNumber,
                       LocalDate checkInDate,
                       LocalDate checkOutDate) {

        if (reservationId == null) {
            throw new IllegalArgumentException("reservationId는 필수 값입니다.");
        }
        if (hotelId == null) {
            throw new IllegalArgumentException("hotelId는 필수 값입니다.");
        }
        if (roomId == null) {
            throw new IllegalArgumentException("roomId는 필수 값입니다.");
        }
        if (guestName == null || guestName.isBlank()) {
            throw new IllegalArgumentException("예약자 이름을 입력해 주세요.");
        }
        if (checkInDate == null || checkOutDate == null) {
            throw new IllegalArgumentException("체크인/체크아웃 날짜를 모두 입력해 주세요.");
        }
        if (!checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("체크아웃 날짜는 체크인 날짜보다 뒤여야 합니다.");
        }

        this.reservationId = reservationId;
        this.hotelId = hotelId;
        this.roomId = roomId;
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public boolean isOverlapped(LocalDate otherCheckIn, LocalDate otherCheckOut) {
        return checkInDate.isBefore(otherCheckOut) && otherCheckIn.isBefore(checkOutDate);
    }
}

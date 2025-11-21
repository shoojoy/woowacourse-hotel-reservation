package hotel.domain;

public class Hotel {

    private final Long hotelId;
    private final String hotelName;
    private final String address;

    public Hotel(Long hotelId, String hotelName, String address) {
        if (hotelId == null) {
            throw new IllegalArgumentException("hotelId는 필수사항 입니다.");
        }
        if (hotelName == null || hotelName.isBlank()) {
            throw new IllegalArgumentException("호텔 이름을 입력하여 주세요.");
        }
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("호텔 주소를 입력하여 주세요.");
        }

        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.address = address;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getAddress() {
        return address;
    }
}

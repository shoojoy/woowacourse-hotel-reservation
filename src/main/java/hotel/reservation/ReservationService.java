package hotel.reservation;

import hotel.domain.Reservation;
import hotel.domain.Room;

import java.time.LocalDate;
import java.util.List;

/**
 * 예약 관련 비즈니스 로직을 담당하는 서비스입니다.
 * 저장소(인메모리)를 주입받아서 사용하게 될 예정입니다.
 */
public class ReservationService {

    public List<Room> findAvailableRooms(
            Long hotelId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int guestCount
    ) {
        // TODO: 추후 구현
        // 1. 해당 호텔의 모든 객실 조회
        // 2. guestCount <= maxGuestCount인 객실만 필터
        // 3. 각 객실에 대해 예약 목록 조회 후, 날짜 겹치지 않는 객실만 반환
        return List.of();
    }

    public Reservation reserve(
            Long hotelId,
            Long roomId,
            String guestName,
            String phoneNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {
        // TODO: 추후 구현
        // 1. 해당 객실 존재 여부 확인
        // 2. 해당 객실의 기존 예약들과 날짜 겹침 여부 확인
        // 3. 문제 없으면 Reservation 생성 + 저장소에 저장
        return null;
    }

    public void cancel(Long reservationId) {
        // TODO: 추후 구현
        // 저장소에서 reservationId로 찾아서 삭제
    }
}

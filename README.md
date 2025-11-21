# 우아한 호텔 예약 시스템

우아한테크코스 오픈 미션으로 만든 **호텔 예약 백엔드 프로젝트**입니다.  
프론트엔드는 구현하지 않고 자바로 도메인 모델과 예약 로직을 설계,구현하는 것에 집중했습니다.

---

## 기능 개요

- 우아한 호텔 지점
    - 우아한 호텔 서울
    - 우아한 호텔 경주
    - 우아한 호텔 부산
- 객실 정보
    - 객실 등급 (STANDARD / DELUXE / SUITE)
    - 객실별 최대 인원, 1박 가격
- 예약 기능
    - 호텔 선택
    - 체크인 / 체크아웃 날짜, 인원 수 입력
    - 예약 가능 객실 조회
    - 객실 선택 + 예약자 정보 입력 후 예약 생성
    - 예약 취소

---

## 기술 스택

- Java (버전 21)
- Gradle
- JUnit (테스트 작성 예정이라면 적어두기)

## 도메인 설계
### 호텔(Hotel)

 - hotelId: 호텔 고유 번호
 - hotelName: 호텔 이름 (예: 우아한 호텔 서울)
 - address: 호텔 주소
 - 객실(Room)
 - roomId: 객실 고유 번호
 - hotelId: 어느 호텔에 속한 객실인지
 - roomNumber: 객실 호수 (예: 101)
 - roomGrade: 객실 등급 (STANDARD/DELUXE/SUITE)
 - maxGuestCount: 최대 수용 인원
 - pricePerNight: 1박 가격
 - 예약(Reservation)
 - reservationId: 예약 고유 번호
 - hotelId, roomId
 - guestName, phoneNumber
 - checkInDate, checkOutDate
 - 날짜 겹침 여부를 판단하는 isOverlapped 메서드 포함

## 테스트 실행
./gradlew test
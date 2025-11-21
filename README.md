# 우아한 호텔 예약 시스템

우아한테크코스 오픈 미션으로 만든 **콘솔 기반 호텔 예약 백엔드 프로젝트**입니다.  
프론트엔드는 구현하지 않고, 자바로 **도메인 모델과 예약 비즈니스 로직**을 설계·구현하는 것에 집중했습니다.

---

## 기능 개요

- **우아한 호텔 지점**
  - 우아한 호텔 서울
  - 우아한 호텔 부산
  - 우아한 호텔 경주

- **객실 정보**
  - 객실 등급: `STANDARD`, `DELUXE`, `SUITE`
  - 객실별 최대 인원 (현재 2명 고정)
  - 등급별 1박 가격 (예: 80,000 / 100,000 / 120,000원)

- **예약 기능**
  - 호텔 선택
  - 체크인 / 체크아웃 날짜, 인원 수 입력
  - 조건에 맞는 예약 가능 객실 조회
  - 객실 선택 + 예약자 정보 입력 후 예약 생성
  - (서비스 레벨에서) 예약 취소

---

## 기술 스택

- Java 21
- Gradle
- JUnit 5 (단위 테스트)

---

### 테스트

### ReservationTest
 - 예약 기간 겹침/비겹침에 대한 isOverlapped 동작 검증
### ReservationServiceTest
 - 예약이 없을 때 모든 객실이 조회되는지
 - 최대 인원 수를 초과하는 경우 객실이 제외되는지
 - 기존 예약과 날짜가 겹치면 예약이 거절되는지
 - 정상적인 예약 생성 & 취소가 동작하는지

## 실행 방법

프로젝트 루트에서 아래 명령어를 사용합니다.

```bash
# 테스트 실행
./gradlew clean test

# 콘솔에서 예약 시나리오 실행
./gradlew run

Windows PowerShell에서 한글이 깨질 경우, 실행 전에 아래 명령어로 코드 페이지를 UTF-8로 변경할 수 있습니다.
chcp 65001

```

## 도메인 설계
### Hotel (호텔)

 - hotelId: 호텔 고유 번호 (Long)
 - hotelName: 호텔 이름 (예: "우아한 호텔 서울")
 - address: 호텔 주소
### Room (객실)
 - roomId: 객실 고유 번호
 - hotelId: 어느 호텔에 속한 객실인지
 - roomNumber: 객실 호수 (예: "101")
 - roomGrade: 객실 등급 (STANDARD / DELUXE / SUITE)
 - maxGuestCount: 최대 수용 인원
 - pricePerNight: 1박 가격
### Reservation (예약)
 - reservationId: 예약 고유 번호
 - hotelId, roomId: 어느 호텔/객실에 대한 예약인지
 - guestName: 예약자 이름
 - phoneNumber: 연락처
 - checkInDate, checkOutDate: 체크인/체크아웃 날짜
 - isOverlapped(LocalDate otherCheckIn, LocalDate otherCheckOut)
 - [checkInDate, checkOutDate) 와 [otherCheckIn, otherCheckOut) 가 겹치는지 여부를 판단하는 메서드
 - 예약 가능 여부를 검사할 때 사용

## 주요 비즈니스 로직

모든 데이터는 인메모리 저장소(InMemory*Storage)에 저장합니다.

### 예약 가능 객실 조회

 - ReservationService.findAvailableRooms(hotelId, checkIn, checkOut, guestCount)
 - 해당 호텔의 모든 객실 조회
 - guestCount <= maxGuestCount 인 객실만 필터링
 - 각 객실의 기존 예약 목록을 조회
 - Reservation.isOverlapped(...) 를 이용해 날짜가 겹치지 않는 객실만 반환

### 예약 생성

 - ReservationService.reserve(hotelId, roomId, guestName, phoneNumber, checkIn, checkOut)
 - 객실 존재 여부 확인
 - 해당 객실에 대해 이미 겹치는 예약이 있는지 확인
 - 문제가 없으면 Reservation 생성 후 인메모리 저장소에 저장
 - 생성된 예약 객체를 반환
 - (예약 번호, 날짜, 예약자 정보를 App에서 출력)

### 예약 취소
 - ReservationService.cancel(reservationId)
 - reservationId로 예약을 조회
 - 존재하면 인메모리 저장소에서 삭제

## 콘솔 예약 시나리오 예시

 - 호텔 목록 출력 후, 호텔 번호 선택
 - 체크인 / 체크아웃 날짜 입력
 - (예: 2025-12-25, 2025-12-26)
 - 투숙 인원 수 입력
 - 조건에 맞는 예약 가능 객실 목록 출력
 - 예약할 roomId 선택
 - 예약자 이름 / 전화번호 입력
 - 예약 완료 메시지 및 예약 정보 출력
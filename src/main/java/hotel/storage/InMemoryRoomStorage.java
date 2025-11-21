package hotel.storage;

import hotel.domain.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryRoomStorage {

    private final Map<Long, Room> rooms = new HashMap<>();

    public void save(Room room) {
        rooms.put(room.getRoomId(), room);
    }

    public Optional<Room> findById(Long roomId) {
        return Optional.ofNullable(rooms.get(roomId));
    }

    public List<Room> findByHotelId(Long hotelId) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.getHotelId().equals(hotelId)) {
                result.add(room);
            }
        }
        return result;
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }

    public void clear() {
        rooms.clear();
    }
}

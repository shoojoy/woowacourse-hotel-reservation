package hotel.storage;

import hotel.domain.Hotel;

import java.util.*;

public class InMemoryHotelStorage {

    private final Map<Long, Hotel> hotels = new HashMap<>();

    public void save(Hotel hotel) {
        hotels.put(hotel.getHotelId(), hotel);
    }

    public Optional<Hotel> findById(Long hotelId) {
        return Optional.ofNullable(hotels.get(hotelId));
    }

    public List<Hotel> findAll() {
        return new ArrayList<>(hotels.values());
    }

    public void clear() {
        hotels.clear();
    }
}

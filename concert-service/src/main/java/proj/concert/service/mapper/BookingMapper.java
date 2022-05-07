package proj.concert.service.mapper;

import proj.concert.common.dto.BookingDTO;
import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Booking;

import java.util.ArrayList;
import java.util.List;

//A class which maps a booking to a bookingDTO

public class BookingMapper {
    private BookingMapper(){}

    public static BookingDTO toDTO(Booking booking){
        List<SeatDTO> seats = new ArrayList<SeatDTO>();
        booking.getSeats().forEach(seat -> seats.add(SeatMapper.toDTO(seat)));
        return new BookingDTO(booking.getId(), booking.getDate(), seats);
    }
}

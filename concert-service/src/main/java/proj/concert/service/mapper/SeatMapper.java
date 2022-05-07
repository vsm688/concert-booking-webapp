package proj.concert.service.mapper;

import proj.concert.common.dto.SeatDTO;
import proj.concert.service.domain.Seat;

//A class which maps a seat to a seatDTO

public class SeatMapper {
    private SeatMapper(){}

    public static SeatDTO toDTO(Seat seat){
        return new SeatDTO(seat.getLabel(), seat.getCost());
    }
}

package proj.concert.service.services;

import proj.concert.common.dto.ConcertInfoSubscriptionDTO;

import javax.ws.rs.container.AsyncResponse;
import java.time.LocalDateTime;
/**
 * class that holds an async response associated with a subscriber as well as the in DTO.
 */

public class SubTuple {

    private AsyncResponse response;
    private ConcertInfoSubscriptionDTO concertInDTO;

    public SubTuple(AsyncResponse r, ConcertInfoSubscriptionDTO c){
        this.response = r;
        this.concertInDTO = c;

    }

    public int getPercentageBooked(){
        // return the booking percentage that was set in the DTO.
        return concertInDTO.getPercentageBooked();
    }
    public AsyncResponse getResponse(){
        return response;
    }

}

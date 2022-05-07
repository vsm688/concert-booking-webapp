package proj.concert.service.services;

import proj.concert.common.dto.ConcertInfoSubscriptionDTO;

import javax.ws.rs.container.AsyncResponse;
import java.time.LocalDateTime;

public class SubTuple {

    private AsyncResponse response;
    private int percentageBooked;

    private ConcertInfoSubscriptionDTO concertInDTO;

    public SubTuple(AsyncResponse r, int p, ConcertInfoSubscriptionDTO c){
        this.response = r;

        this.percentageBooked = p;

        this.concertInDTO = c;

    }

    public int getPercentageBooked(){
        return percentageBooked;
    }
    public AsyncResponse getResponse(){
        return response;
    }

}

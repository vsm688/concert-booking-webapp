package proj.concert.service.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

//Identify seat uniquely via label and date ( for a specific concert ).

@Embeddable
public class SeatCompositeKey implements Serializable {


    private String label;
    private LocalDateTime date;

    public SeatCompositeKey(){

    }

    public SeatCompositeKey(String label, LocalDateTime date){
        this.label = label;
        this.date = date;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
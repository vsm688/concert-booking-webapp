package proj.concert.service.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *  Class that represents a composite key which uses label and date to uniquely identify a seat.
 *
 */
//Identify seat uniquely via label and date ( for a specific concert ).

@Embeddable
public class SeatCompositeKey implements Serializable {
    private String label;
    private LocalDateTime date;

    public SeatCompositeKey(){}

    public SeatCompositeKey(String label, LocalDateTime date){
        this.label = label;
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SeatCompositeKey)) {
            return false;
        }
        SeatCompositeKey other = (SeatCompositeKey) obj;
        return Objects.equals(label, other.label) &&
                Objects.equals(date, other.date);
    }
}
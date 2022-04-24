package proj.concert.service.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
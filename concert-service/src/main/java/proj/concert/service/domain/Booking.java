package proj.concert.service.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.dto.SeatDTO;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *  Class that represents a concert booking for a user
 *
 */


@Entity
@Table(name = "BOOKINGS")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A user can make many bookings
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    private long concertId;
    private LocalDateTime date;

    // A booking can have many seats.
    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<Seat> seats = new HashSet<>();
    public Booking(){}

    public Booking(Long id, User user, Concert concert){
        this.id = id;
        this.user = user;
    }

    public Booking (User user, Concert concert){
        this(null, user, concert);

    }

    public Booking(long concertId, LocalDateTime date, Set<Seat> seats){
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, user, date);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(user, other.user) &&
                Objects.equals(date, other.date);
    }


}

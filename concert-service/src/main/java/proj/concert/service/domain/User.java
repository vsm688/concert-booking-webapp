package proj.concert.service.domain;


import javax.persistence.*;
import java.awt.print.Book;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 *  Class that represents a User which can create bookings.
 *
 */
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    @Version
    private Long version;

    @Column(unique = true)
    private UUID sessionId;

    // Owner class/instance is a user (mappedBy). We additionally use CascadeType.REMOVE to delete all associated bookings
    // if a user is deleted
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "user")
    private Set<Booking> bookings = new HashSet<>();

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }




    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(username, other.username) &&
                Objects.equals(password, other.password);
    }

    public Set<Booking> getBookings() {
        return bookings;
    }
}
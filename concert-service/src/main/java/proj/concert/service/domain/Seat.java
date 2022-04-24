package proj.concert.service.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "SEATS")
@IdClass(SeatCompositeKey.class)
public class Seat{
	@Id
    private String label;
	private boolean isBooked;
	@Id
	private LocalDateTime date;
	private BigDecimal cost;

	// if a seat has not been booked, its bookingID will be null. this does not prevent a seat from being booked twice.
	@ManyToOne
	@JoinColumn(name = "Booking_ID")
	private Booking booking;

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal cost) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.cost = cost;
	}

	public Seat() {}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, label, cost);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Seat)) {
			return false;
		}
		Seat other = (Seat) obj;
		return Objects.equals(label, other.label) &&
				Objects.equals(isBooked, other.isBooked) &&
				Objects.equals(date, other.date) &&
				Objects.equals(cost, other.cost);
	}
}


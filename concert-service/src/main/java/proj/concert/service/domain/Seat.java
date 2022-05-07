package proj.concert.service.domain;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *  Class that represents a seat within a concert.
 *  It is uniquely identified by a combination of label and date ( links it to a specific concert )
 *
 */
@Entity
@Table(name = "SEATS")
@IdClass(SeatCompositeKey.class)
public class Seat {
	// Version used for optimistic concurrency control
	@Version
	private long version;
	@Id
	@Column(name = "LABEL")
	private String label;
	@Column(name = "COST")
	private BigDecimal cost;

	@Id
	@Column(name = "DATE")
	private LocalDateTime date;

	private Boolean isBooked;


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

	public Boolean getIsBooked(){
		return isBooked;
	}

	public void setBooked(Boolean booked) {
		isBooked = booked;
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


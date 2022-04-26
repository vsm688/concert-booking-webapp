package proj.concert.service.domain;

import org.hibernate.engine.internal.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name ="SEATS")
@IdClass(SeatCompositeKey.class)
public class Seat {



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

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
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

}


package proj.concert.service.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class Seat{

    private String label;
	private boolean isBooked;
	private LocalDateTime date;
	private BigDecimal cost;

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal cost) {	
	}	
	
	public Seat() {}	
}

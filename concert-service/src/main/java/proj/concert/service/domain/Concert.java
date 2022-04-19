package proj.concert.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

@Entity
public class Concert implements Comparable<Concert>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Set<LocalDateTime> dates =  new HashSet<LocalDateTime>();

    @ManyToMany(cascade = {CascadeType.ALL})
    private Set<Performer> performers = new HashSet<Performer>();

    public Concert(Long id, String title, Set<LocalDateTime> dates){
        this.id  =id;
        this.title = title;
        this.dates = dates;
    }

    public Concert() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public Set<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(Set<LocalDateTime> dates){
        this.dates = dates;
    }

    public Set<Performer> getPerformers(){
        return performers;
    }

    public void setPerformers(Set<Performer> performers){
        this.performers = performers;
    }
    public int compareTo(Concert concert) {
        return title.compareTo(concert.title);
    }




}

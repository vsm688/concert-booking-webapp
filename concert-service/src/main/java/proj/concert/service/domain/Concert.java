package proj.concert.service.domain;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;

@Entity
public class Concert implements Comparable<Concert>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String image_name;
    private String blurb;

    @Transient
    private Set<LocalDateTime> dates =  new HashSet<LocalDateTime>();

    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "concerts")
    private Set<Performer> performers = new HashSet<Performer>();

    @Transient
    private Set<Booking> bookings = new HashSet<Booking>();

    public Concert(Long id, String title, Set<LocalDateTime> dates, String image_name, String blurb){
        this.id  = id;
        this.image_name = image_name;
        this.blurb = blurb;
        this.title = title;
        this.dates = dates;
    }

    public Concert() {
    }

    public Long getId() {
        return id;
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


    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }
}

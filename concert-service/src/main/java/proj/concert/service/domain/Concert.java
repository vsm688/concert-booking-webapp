package proj.concert.service.domain;

import java.awt.print.Book;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import proj.concert.service.jaxrs.LocalDateTimeParam;
/**
 *  Class that represents a concert
 *
 */
@Entity
@Table(name = "CONCERTS")
public class Concert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String image_name;

    @Lob
    private String blurb;
    //    CONCERT_DATES associative table, which represents that one concert being associated with many dates.
    //    We assume dates cannot be duplicated.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CONCERT_DATES", joinColumns = @JoinColumn(name = "CONCERT_ID"))
    @Column(name = "DATE", unique = true)
    private Set<LocalDateTime> dates =  new HashSet<>();

    //   FetchType Lazy because we don't need to load performers every time a concert is loaded.
    //   Associative table that links Many concerts to Many performers by storing concertID and PerformerID in each row
    //   Does this with @JoinColumn.
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name ="CONCERT_PERFORMER", joinColumns = @JoinColumn(name = "CONCERT_ID"), inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID"))
    private Set<Performer> performers = new HashSet<>();



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

    public void setPerformers(Set<Performer> performers) {this.performers = performers;}

    public Set<Performer> getPerformers() {return performers;}

    @Override
    public int hashCode() {
        return Objects.hash(id, title, dates, image_name, blurb);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Concert)) {
            return false;
        }
        Concert other = (Concert) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(title, other.title) &&
                Objects.equals(dates, other.dates) &&
                Objects.equals(image_name, other.image_name) &&
                Objects.equals(blurb, other.blurb);
    }

}

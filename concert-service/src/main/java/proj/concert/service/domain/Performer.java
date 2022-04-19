package proj.concert.service.domain;

import proj.concert.common.jackson.types.BookingStatus;
import proj.concert.common.jackson.types.Genre;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Performer {
    private Long id;
    private String blurb;
    private String imageURI;
    private Genre genre;

    @ManyToMany(mappedBy = "performers")
    private Set<Concert> concerts = new HashSet<Concert>();

    public Performer(){}

    public Performer(Long id, String blurb, String imageURI, Genre genre){
        this.id = id;
        this.blurb = blurb;
        this.imageURI = imageURI;
        this.genre = genre;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Set<Concert> getConcerts() {
        return concerts;
    }

    public void setConcerts(Set<Concert> concerts) {
        this.concerts = concerts;
    }
}

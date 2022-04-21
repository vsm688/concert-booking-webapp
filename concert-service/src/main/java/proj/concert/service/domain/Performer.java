package proj.concert.service.domain;

import proj.concert.common.jackson.types.Genre;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String blurb;
    private String image_name;
    private Genre genre;

    @ManyToMany(mappedBy = "performers")
    private Set<Concert> concerts = new HashSet<Concert>();

    public Performer(){}

    public Performer(Long id, String blurb, String imageURI, Genre genre){
        this.id = id;
        this.blurb = blurb;
        this.image_name = imageURI;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }



    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
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

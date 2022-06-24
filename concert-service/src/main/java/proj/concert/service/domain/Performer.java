package proj.concert.service.domain;

import proj.concert.common.jackson.types.Genre;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents a performer
 *
 **/

@Entity
@Table(name = "PERFORMERS")
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // Using @Lob since blurb can be very long.
    @Lob
    private String blurb;

    @Column(name = "IMAGE_NAME")
    private String image_name;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public Performer(){}

    public Performer(Long id, String name, String blurb, String imageURI, Genre genre){

        this.id = id;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blurb, image_name, genre);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Performer)) {
            return false;
        }
        Performer other = (Performer) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(blurb, other.blurb) &&
                Objects.equals(image_name, other.image_name) &&
                Objects.equals(genre, other.genre);

    }

}

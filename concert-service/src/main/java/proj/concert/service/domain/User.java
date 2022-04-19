package proj.concert.service.domain;


import javax.persistence.Embeddable;

@Embeddable
public class User {

    private Long id;
    private String username;
    private String password;
    private long version;


}

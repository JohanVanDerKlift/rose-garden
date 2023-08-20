package nl.johanvanderklift.roseGarden.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue
    private Long id;
    private String filename;
    @Lob
    private byte[] docFile;

    @OneToOne
    @JoinColumn(name = "user_username")
    private User user;

}

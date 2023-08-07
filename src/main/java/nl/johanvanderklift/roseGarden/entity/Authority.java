package nl.johanvanderklift.roseGarden.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @Column(name = "authority_name", nullable = false)
    private String authorityName;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users = new ArrayList<>();

}
package nl.johanvanderklift.roseGarden.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @Column(name = "authority_name", nullable = false)
    private String authorityName;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users = new ArrayList<>();

}
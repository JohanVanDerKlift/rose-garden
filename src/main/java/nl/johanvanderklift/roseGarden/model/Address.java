package nl.johanvanderklift.roseGarden.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "zipcode", nullable = false)
    private String zipcode;

    @Column(name = "city", nullable = false)
    private String city;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

}
package nl.johanvanderklift.roseGarden.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "availability", nullable = false)
    private Boolean availability = false;

    @Column(name = "tax")
    private Double tax;

    @JsonIgnore
    @OneToOne(mappedBy = "product", orphanRemoval = true)
    private WebOrderDetail webOrderDetail;

}
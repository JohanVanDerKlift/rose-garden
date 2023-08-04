package nl.johanvanderklift.roseGarden.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @OneToOne(mappedBy = "product", orphanRemoval = true)
    private WebOrderDetail webOrderDetail;

}
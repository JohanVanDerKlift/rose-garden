package nl.johanvanderklift.roseGarden.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "web_order_detail")
public class WebOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "web_order_id")
    private WebOrder webOrder;

}
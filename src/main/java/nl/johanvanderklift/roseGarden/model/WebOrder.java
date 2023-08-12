package nl.johanvanderklift.roseGarden.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "web_order")
public class WebOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "web_order_status", nullable = false)
    private WebOrderStatus webOrderStatus;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @OneToMany(mappedBy = "webOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WebOrderDetail> webOrderDetails = new ArrayList<>();

    public Double getTax() {
        double tax = 0.00;
        for (WebOrderDetail webOrderDetail : this.webOrderDetails) {
            tax += (webOrderDetail.getProduct().getPrice() * ((webOrderDetail.getProduct().getTax() / 100)))
                    * webOrderDetail.getQuantity();
        }
        return tax;
    }

    public Double getTotalPrice() {
        double totalPrice = 0.00;
        for (WebOrderDetail webOrderDetail : webOrderDetails) {
            totalPrice += webOrderDetail.getProduct().getPrice() * webOrderDetail.getQuantity();
        }
        return totalPrice;
    }

    public Double getTotalPriceExTax() {
        return getTotalPrice() - getTax();
    }

}
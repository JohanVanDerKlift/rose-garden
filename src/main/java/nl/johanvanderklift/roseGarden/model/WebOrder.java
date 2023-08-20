package nl.johanvanderklift.roseGarden.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "web_order")
public class WebOrder {

    @Id
    @GeneratedValue(generator = "web_order_gen")
    @GenericGenerator(
            name = "web_order_gen",
            strategy = "nl.johanvanderklift.roseGarden.utils.CustomWebOrderIdGenerator")
    @Column(name = "id", nullable = false)
    private String id;

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

    @Column(name = "order_date_time")
    private LocalDateTime orderDateTime;


    public Double getTax() {
        double tax = 0.00;
        for (WebOrderDetail webOrderDetail : this.webOrderDetails) {
            tax += (webOrderDetail.getProduct().getPrice() / 121) * webOrderDetail.getProduct().getTax()
                    * webOrderDetail.getQuantity();
        }
        return Math.round(tax * 100) / 100.00;
    }

    public Double getTotalPrice() {
        double totalPrice = 0.00;
        for (WebOrderDetail webOrderDetail : webOrderDetails) {
            totalPrice += webOrderDetail.getProduct().getPrice() * webOrderDetail.getQuantity();
        }
        return totalPrice;
    }

    public Double getTotalPriceExTax() {
        return (double) Math.round(getTotalPrice() - getTax());
    }

}
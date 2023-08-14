package nl.johanvanderklift.roseGarden.dto;

import nl.johanvanderklift.roseGarden.model.Address;
import nl.johanvanderklift.roseGarden.model.WebOrderDetail;

import java.util.List;

public class WebOrderOutputDto {

    public String id;
    public String webOrderStatus;
    public Address address;
    public Double totalPriceExTax;
    public Double tax;
    public Double totalPrice;
    public List<WebOrderDetail> webOrderDetails;

}

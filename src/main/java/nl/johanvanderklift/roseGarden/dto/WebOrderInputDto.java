package nl.johanvanderklift.roseGarden.dto;

import nl.johanvanderklift.roseGarden.model.WebOrderStatus;

public class WebOrderInputDto {

    public WebOrderStatus webOrderStatus;
    public Long addressId;
    public Integer quantity;
    public Long productId;

}

package nl.johanvanderklift.roseGarden.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.johanvanderklift.roseGarden.model.User;

public class AddressInputDto {
    @NotNull
    @NotBlank
    public String addressLine1;
    public String addressLine2;
    @NotNull
    @NotBlank
    public String zipcode;
    @NotNull
    @NotBlank
    public String city;
    public User user;
}

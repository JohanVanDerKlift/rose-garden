package nl.johanvanderklift.roseGarden.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductInputDto {

    public Double price;
    @NotNull
    @NotBlank
    public String name;
    public String description;
    public Boolean availability;
    public Double tax;
}

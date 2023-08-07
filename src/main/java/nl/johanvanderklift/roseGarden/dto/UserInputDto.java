package nl.johanvanderklift.roseGarden.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import nl.johanvanderklift.roseGarden.entity.Authority;

import java.util.List;

public class UserInputDto {

    @NotBlank
    @NotNull
    @Email
    public String email;
    @NotBlank
    @NotNull
    // Minimum eight characters, at least one uppercase letter, one lowercase letter and one number:
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    public String password;
    @NotBlank
    @NotNull
    public String firstName;
    @NotBlank
    @NotNull
    public String lastName;
    @NotBlank
    @NotNull
    public String companyName;
    @NotBlank
    @NotNull
    public String phoneNumber;
    public List<Authority> authorities;

}

package nl.johanvanderklift.roseGarden.dto;

import nl.johanvanderklift.roseGarden.entity.Authority;

import java.util.List;

public class UserOutputDto {

    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String companyName;
    public String phoneNumber;
    public Boolean hasCredit;
    public List<Authority> authorities;

}

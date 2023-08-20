package nl.johanvanderklift.roseGarden.dto;

import nl.johanvanderklift.roseGarden.model.Address;
import nl.johanvanderklift.roseGarden.model.Authority;

import java.util.ArrayList;
import java.util.List;

public class UserOutputDto {

    public String username;
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String companyName;
    public String phoneNumber;
    public Boolean hasCredit;
    public List<Authority> authorities = new ArrayList<>();
    public List<Address> addresses = new ArrayList<>();

}

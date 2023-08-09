package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.AddressInputDto;
import nl.johanvanderklift.roseGarden.dto.AddressOutputDto;
import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.model.Address;
import nl.johanvanderklift.roseGarden.model.Authority;
import nl.johanvanderklift.roseGarden.model.User;
import nl.johanvanderklift.roseGarden.exception.AuthorityNotFoundException;
import nl.johanvanderklift.roseGarden.exception.UserNotFoundException;
import nl.johanvanderklift.roseGarden.repository.AddressRepository;
import nl.johanvanderklift.roseGarden.repository.AuthorityRepository;
import nl.johanvanderklift.roseGarden.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, AddressRepository addressRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.addressRepository = addressRepository;
        this.encoder = encoder;
    }

    public List<UserOutputDto> getAllUsers() {
        List<UserOutputDto> dtos = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            dtos.add(transferUserToDto(user));
        }
        return dtos;
    }

    public UserOutputDto getUserByUsername(String username) {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            User user = opUser.get();
            return transferUserToDto(user);
        }
    }

    public String createUser(UserInputDto dto) {
        User user = new User();
        userRepository.save(transferDtoToUser(dto, user));
        return user.getUsername();
    }

    public void addAuthorityToUser(String username, String authority) {
        Optional<User> opUser = userRepository.findById(username);
        Optional<Authority> opAuthority = authorityRepository.findById("ROLE_" + authority);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } if (opAuthority.isEmpty()) {
            throw new AuthorityNotFoundException(authority);
        } else {
            User user = opUser.get();
            Authority auth = opAuthority.get();
            user.addAuthority(auth);
            userRepository.save(user);
        }
    }

    public void removeAuthority(String username, String authority) {
        Optional<User> opUser = userRepository.findById(username);
        Optional<Authority> opAuthority = authorityRepository.findById(authority);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } if (opAuthority.isEmpty()) {
            throw new AuthorityNotFoundException(authority);
        } else {
            User user = opUser.get();
            Authority auth = opAuthority.get();
            user.removeAuthority(auth);
            userRepository.save(user);
        }
    }

    public Long addAddressToUser(AddressInputDto dto, User user) {
        Address address = new Address();
        address.setAddressLine1(dto.addressLine1);
        address.setAddressLine2(dto.addressLine2);
        address.setZipcode(dto.zipcode);
        address.setCity(dto.city);
        address.setUser(user);
        addressRepository.save(address);
        return address.getId();
    }

    private UserOutputDto transferUserToDto(User user) {
        UserOutputDto dto = new UserOutputDto();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.password = user.getPassword();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.companyName = user.getCompanyName();
        dto.phoneNumber = user.getPhoneNumber();
        dto.hasCredit = user.getHasCredit();
        dto.authorities.addAll(user.getAuthorities());
        return dto;
    }

    private User transferDtoToUser(UserInputDto dto, User user) {
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(encoder.encode(dto.password));
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setCompanyName(dto.companyName);
        user.setPhoneNumber(dto.phoneNumber);
        return user;
    }
}

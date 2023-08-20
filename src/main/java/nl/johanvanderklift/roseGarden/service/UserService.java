package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.AddressInputDto;
import nl.johanvanderklift.roseGarden.dto.AddressOutputDto;
import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.exception.AuthorityAlreadyPresentException;
import nl.johanvanderklift.roseGarden.exception.LastAdminException;
import nl.johanvanderklift.roseGarden.model.Address;
import nl.johanvanderklift.roseGarden.model.Authority;
import nl.johanvanderklift.roseGarden.model.User;
import nl.johanvanderklift.roseGarden.exception.AuthorityNotFoundException;
import nl.johanvanderklift.roseGarden.exception.UserNotFoundException;
import nl.johanvanderklift.roseGarden.repository.AddressRepository;
import nl.johanvanderklift.roseGarden.repository.AuthorityRepository;
import nl.johanvanderklift.roseGarden.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
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

    public String updateUser(UserInputDto dto, String username) {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            User user = opUser.get();
            userRepository.save(transferDtoToUser(dto, user));
            return user.getUsername();
        }
    }

    public void toggleHasCredit(String username) {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            User user = opUser.get();
            user.setHasCredit(!user.getHasCredit());
            userRepository.save(user);
        }
    }

    public void removeUser(String username) {
        Optional<User> opUser = userRepository.findById(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else {
            User user = opUser.get();
            if (user.getAuthorities().contains(authorityRepository.findById("ROLE_ADMIN").get())) {
                List<User> users = userRepository.findAll();
                int count = 0;
                for (User userToCheck : users) {
                    if (userToCheck.getAuthorities().contains(authorityRepository.findById("ROLE_ADMIN").get())) {
                        count += 1;
                    }
                }
                if (count <= 1) {
                    throw new LastAdminException();
                }
            } else {
                userRepository.delete(user);
            }
        }
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
            if (user.getAuthorities().contains(auth)) {
                throw new AuthorityAlreadyPresentException(auth.getAuthorityName());
            } else {
                user.addAuthority(auth);
                userRepository.save(user);
            }
        }
    }

    public void removeAuthority(String username, String authority) {
        Optional<User> opUser = userRepository.findById(username);
        Optional<Authority> opAuthority = authorityRepository.findById(authority);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(username);
        } else if (opAuthority.isEmpty()) {
            throw new AuthorityNotFoundException(authority);
        } else {
            User user = opUser.get();
            Authority auth = opAuthority.get();
            user.removeAuthority(auth);
            userRepository.save(user);
        }
    }

    public List<AddressOutputDto> getAllAddressesByUser(UserDetails userDetails) {
        List<Address> addresses = addressRepository.findByUser_UsernameOrderByIdAsc(userDetails.getUsername());
        List<AddressOutputDto> dtos = new ArrayList<>();
        for (Address address: addresses) {
            AddressOutputDto dto = new AddressOutputDto();
            dto.addressLine1 = address.getAddressLine1();
            dto.addressLine2 = address.getAddressLine2();
            dto.city = address.getCity();
            dto.zipcode = address.getZipcode();
            dtos.add(dto);
        }
        return dtos;
    }

    public Long addAddressToUser(AddressInputDto dto, UserDetails userdetails) {
        User user = userRepository.findById(userdetails.getUsername()).orElseThrow();
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
        dto.addresses.addAll(user.getAddresses());
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
        user.setHasCredit(false);
        return user;
    }
}

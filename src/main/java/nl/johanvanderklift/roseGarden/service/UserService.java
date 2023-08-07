package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.entity.Authority;
import nl.johanvanderklift.roseGarden.entity.User;
import nl.johanvanderklift.roseGarden.exception.AuthorityNotFoundException;
import nl.johanvanderklift.roseGarden.exception.UserNotFoundException;
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
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
    }

    public List<UserOutputDto> getAllUsers() {
        List<UserOutputDto> dtos = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            dtos.add(transferUserToDto(user));
        }
        return dtos;
    }

    public UserOutputDto getUserByEmail(String email) {
        Optional<User> opUser = userRepository.findById(email);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(email);
        } else {
            User user = opUser.get();
            return transferUserToDto(user);
        }
    }

    public String createUser(UserInputDto dto) {
        User user = new User();
        userRepository.save(transferDtoToUser(dto, user));
        return user.getEmail();
    }

    public void addAuthority(String email, String authority) {
        Optional<User> opUser = userRepository.findById(email);
        Optional<Authority> opAuthority = authorityRepository.findById(authority);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(email);
        } if (opAuthority.isEmpty()) {
            throw new AuthorityNotFoundException(authority);
        } else {
            User user = opUser.get();
            Authority auth = opAuthority.get();
            user.addAuthority(auth);
            userRepository.save(user);
        }
    }

    public void removeAuthority(String email, String authority) {
        Optional<User> opUser = userRepository.findById(email);
        Optional<Authority> opAuthority = authorityRepository.findById(authority);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException(email);
        } if (opAuthority.isEmpty()) {
            throw new AuthorityNotFoundException(authority);
        } else {
            User user = opUser.get();
            Authority auth = opAuthority.get();
            user.removeAuthority(auth);
            userRepository.save(user);
        }
    }

    private UserOutputDto transferUserToDto(User user) {
        UserOutputDto dto = new UserOutputDto();
        dto.email = user.getEmail();
        dto.firstName = user.getFirstName();
        dto.lastName = user.getLastName();
        dto.companyName = user.getCompanyName();
        dto.phoneNumber = user.getPhoneNumber();
        dto.hasCredit = user.getHasCredit();
        dto.authorities.addAll(user.getAuthorities());
        return dto;
    }

    private User transferDtoToUser(UserInputDto dto, User user) {
        user.setEmail(dto.email);
        user.setPassword(encoder.encode(dto.password));
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setCompanyName(dto.companyName);
        user.setPhoneNumber(dto.phoneNumber);
        user.setAuthorities(dto.authorities);
        return user;
    }
}

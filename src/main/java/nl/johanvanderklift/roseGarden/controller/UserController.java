package nl.johanvanderklift.roseGarden.controller;

import jakarta.validation.Valid;
import nl.johanvanderklift.roseGarden.dto.AddressInputDto;
import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.model.User;
import nl.johanvanderklift.roseGarden.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<UserOutputDto> getUserByEmail(@RequestParam String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserInputDto dto, BindingResult br) {
        if (br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(getBindingResult(br));
        } else {
            String username = userService.createUser(dto);
            userService.addAuthorityToUser(username, "USER");
            return new ResponseEntity<>(username, HttpStatus.CREATED);
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<String> addAuthorityToToUser(@RequestParam String auth, @RequestParam String username) {
        userService.addAuthorityToUser(username, auth);
        return ResponseEntity.ok().body(auth);
    }

    @PostMapping("/address")
    public ResponseEntity<Long> AddAddressToUser(@Valid @RequestBody AddressInputDto dto, @AuthenticationPrincipal User user) {
        Long addressId = userService.addAddressToUser(dto, user);
        return ResponseEntity.ok(addressId);
    }

    private String getBindingResult(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }

}

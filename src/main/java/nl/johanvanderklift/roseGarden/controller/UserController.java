package nl.johanvanderklift.roseGarden.controller;

import jakarta.validation.Valid;
import nl.johanvanderklift.roseGarden.dto.AddressInputDto;
import nl.johanvanderklift.roseGarden.dto.AddressOutputDto;
import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<UserOutputDto> getUserByUsername(@RequestParam String username) {
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

    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody UserInputDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userService.updateUser(dto, userDetails.getUsername());
        return ResponseEntity.ok(username);
    }

    @PutMapping("/admin/{username}")
    public ResponseEntity<String> updateUserAsAdmin(@RequestBody UserInputDto dto, @PathVariable String username) {
        String returnUsername = userService.updateUser(dto, username);
        return ResponseEntity.ok(returnUsername);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        userService.removeUser(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admin")
    public ResponseEntity<Object> deleteUserAsAdmin(@RequestParam String username) {
        userService.removeUser(username);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<String> addAuthorityToToUser(@RequestParam String auth, @RequestParam String username) {
        userService.addAuthorityToUser(username, auth);
        return ResponseEntity.ok().body(auth);
    }

    @DeleteMapping("/auth")
    public ResponseEntity<String> removeAuthorityFromUser(@RequestParam String auth, @RequestParam String username) {
        userService.removeAuthority(username, auth);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressOutputDto>> getAllAddressesByUser(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(userService.getAllAddressesByUser(user));
    }

    @PostMapping("/address")
    public ResponseEntity<Long> AddAddressToUser(@Valid @RequestBody AddressInputDto dto, @AuthenticationPrincipal UserDetails user) {
        Long addressId = userService.addAddressToUser(dto, user);
        return ResponseEntity.ok(addressId);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        userService.deleteAddressFromUser(id);
        return ResponseEntity.ok().build();
    }

    private String getBindingResult(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }

}

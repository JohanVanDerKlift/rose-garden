package nl.johanvanderklift.roseGarden.controller;

import nl.johanvanderklift.roseGarden.dto.UserInputDto;
import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails ud = (UserDetails) auth.getPrincipal();
            return "Hello " + ud.getUsername();
        } else {
            return "Hello There!";
        }
    }

    @GetMapping("/secret")
    public String tellSecret() {
        return "This is very secret...";
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/search")
    public ResponseEntity<UserOutputDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserInputDto dto, BindingResult br) {
        if (br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(getBindingResult(br));
        } else {
            String username = userService.createUser(dto);
            userService.addAuthority(username, "ROLE_USER");
            return new ResponseEntity<>(username, HttpStatus.CREATED);
        }
    }

    private String getBindingResult(BindingResult br) {
        StringBuilder sb = new StringBuilder();
        for (FieldError fe : br.getFieldErrors()) {
            sb.append(fe.getField()).append(": ").append(fe.getDefaultMessage()).append("\n");
        }
        return sb.toString();
    }

}

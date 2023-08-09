package nl.johanvanderklift.roseGarden.service;

import nl.johanvanderklift.roseGarden.dto.UserOutputDto;
import nl.johanvanderklift.roseGarden.model.Authority;
import nl.johanvanderklift.roseGarden.repository.AuthorityRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;
    private final AuthorityRepository authorityRepository;

    public CustomUserDetailService(UserService userService, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserOutputDto dto = userService.getUserByUsername(username);
        String password = dto.password;
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Authority authority : authorityRepository.findByUsers_Username(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthorityName()));
        }
        return new User(username, password, grantedAuthorities);
    }
}

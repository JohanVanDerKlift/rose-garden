package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface AuthorityRepository extends CrudRepository<Authority, String> {
    List<Authority> findByUsers_Username(String username);



}

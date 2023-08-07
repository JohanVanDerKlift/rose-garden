package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.entity.Authority;
import org.springframework.data.repository.ListCrudRepository;

public interface AuthorityRepository extends ListCrudRepository<Authority, String> {
}

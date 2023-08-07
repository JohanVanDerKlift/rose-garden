package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.entity.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, String> {
}

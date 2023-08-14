package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.User;
import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, String> {
}

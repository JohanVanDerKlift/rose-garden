package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.Address;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface AddressRepository extends ListCrudRepository<Address, Long> {
    List<Address> findByUser_UsernameOrderByIdAsc(String username);

}

package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.Address;
import org.springframework.data.repository.ListCrudRepository;

public interface AddressRepository extends ListCrudRepository<Address, Long> {

}

package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

public interface WebOrderRepository extends ListCrudRepository<WebOrder, Long> {

}

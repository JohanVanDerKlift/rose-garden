package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderRepository extends ListCrudRepository<WebOrder, String> {
    List<WebOrder> findByUser_Username(String username);

}

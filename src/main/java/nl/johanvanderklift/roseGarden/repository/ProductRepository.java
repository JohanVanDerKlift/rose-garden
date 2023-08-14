package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
    List<Product> getByNameContainsIgnoreCase(String name);

}
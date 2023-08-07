package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
    List<Product> getByNameContainsIgnoreCase(String name);

}
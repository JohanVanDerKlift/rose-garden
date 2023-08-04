package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product, Long> {
}
package nl.johanvanderklift.roseGarden.repository;

import nl.johanvanderklift.roseGarden.model.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}

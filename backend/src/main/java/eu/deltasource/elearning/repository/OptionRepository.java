package eu.deltasource.elearning.repository;

import eu.deltasource.elearning.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {

}

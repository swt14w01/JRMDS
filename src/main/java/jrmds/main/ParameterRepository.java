package jrmds.main;

import jrmds.model.Parameter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
  * Repository for the parameters. Provides various functions.
 */
@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Long> {
	Parameter findByName(String name);
}

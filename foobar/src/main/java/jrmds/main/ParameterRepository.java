package jrmds.main;

import jrmds.model.Parameter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Long> {
	Parameter findByName(String name);
}

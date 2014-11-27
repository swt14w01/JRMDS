package jrmds.main;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.Parameter;

@Repository
public interface ParameterRepository extends CrudRepository<Parameter, Long> {
	Parameter findByName(String name);
}

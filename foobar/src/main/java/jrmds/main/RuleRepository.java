package jrmds.main;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.Component;
import jrmds.model.ComponentType;


@Repository
public interface RuleRepository extends CrudRepository<Component, Long> {
	Component findByRefID(String refID, ComponentType type);
	@Query("MATCH (n)-[r]-(m) WHERE r='DEPENDSON' RETURN m")
	Component findByRelation(ComponentType type);
}

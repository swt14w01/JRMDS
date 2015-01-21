package jrmds.main;

import java.util.Set;

import jrmds.model.Project;
import jrmds.model.RegistredUser;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
/**
 * Repository for projects. Provides various functions.
 */

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	Set<Project> findAllByName(String name);
	Project findByName(String name);
	@Query("match (p:Project)--(r:RegistredUser) WHERE p.name={0} return r")
	Set<RegistredUser> findUsers(String name);
	
}
package jrmds.main;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.Project;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	Set<Project> findAllByName(String name);
	Project findByName(String name);
}
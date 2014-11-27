package jrmds.main;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.Project;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	Project findByName(String name);
}
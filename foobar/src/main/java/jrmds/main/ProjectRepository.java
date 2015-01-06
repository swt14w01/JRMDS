package jrmds.main;

import java.util.Set;

import jrmds.model.Project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
	Set<Project> findAllByName(String name);
	Project findByName(String name);
}
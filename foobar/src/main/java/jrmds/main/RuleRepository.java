package jrmds.main;

import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.Component;
import jrmds.model.ComponentType;


@Repository
public interface RuleRepository extends CrudRepository<Component, Long> {
	@Query("match (n:Project {name:{0}})--(m:Component {refID:{1} , type:{2}}) return m;")
	Component findByRefIDAndType(String projectName, String refID, ComponentType type);
	@Query("match (n:Component)-[r:DEPENDSON]->(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findUpstreamRefs(String projectName, String refID);
	@Query("match (n:Component)<-[r:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findDownstreamRefs(String projectName, String refID);
	
}

package jrmds.main;

import java.util.Set;

import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Project;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for components. Provides various functions.
 */
@Repository
public interface RuleRepository extends CrudRepository<Component, Long> {
	@Query("match (n:Component) return n;")
	Set<Component> findAll();
	@Query("match (p:Project)--(n:Component) WHERE p.name={0} RETURN n;")
	Set<Component> findAnyComponentOfProject(String projectName);
	@Query("match (n:Project {name:{0}})--(m:Component {refID:{1}, type:{2}}) return m;")
	Component findByRefIDAndType(String projectName, String refID, ComponentType type);
	@Query("match (n:Component)-[:DEPENDSON]->(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findUpstreamRefs(String projectName, String refID);
	@Query("match (n:Component)<-[:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findDownstreamRefs(String projectName, String refID);
	@Query("match (p:Project)--(n) where id(n)={0} return p")
	Project findProjectContaining(Long id);
	@Query("match (p:Project)--(n:Component)<-[:DEPENDSON*1..]-(m:Component) WHERE p.name={0} AND n.refID={1} AND m.refID={2} return n limit 1")
	Component findAnyConnectionBetween(String projectName, String firstRefID, String secondRefID);
	@Query("MATCH q=(p:Project)--(n:Component)-[:DEPENDSON*1..]->(m:Component) WHERE n.refID={1} AND p.name={0} UNWIND FILTER(n in nodes(q) WHERE n:Component) AS comp RETURN DISTINCT comp;")
	Set<Component> findAllReferencedNodes(String projectName, String refID);
	@Query("match (p:Project)--(n:Component)-->(m:Component)<--(:Component) WHERE p.name={0} AND n.refID={1} return m")
	Set<Component> findSingleReferencedNodes(String projectName, String refID);
}

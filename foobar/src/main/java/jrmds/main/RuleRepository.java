package jrmds.main;

import java.util.Set;

import jrmds.model.Component;
import jrmds.model.ComponentType;
import jrmds.model.Project;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RuleRepository extends CrudRepository<Component, Long> {
	@Query("match (p:Project)--(n:Component) WHERE p.name={0} RETURN n;")
	Set<Component> findAnyComponentOfProject(String projectName);
	@Query("match (n:Project {name:{0}})--(m:Component {refID:{1}, type:{2}}) return m;")
	Component findByRefIDAndType(String projectName, String refID, ComponentType type);
	@Query("match (n:Component)-[r:DEPENDSON]->(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findUpstreamRefs(String projectName, String refID);
	@Query("match (n:Component)<-[r:DEPENDSON]-(m:Component {refID:{1}})--(p:Project {name:{0}}) return n;")
	Set<Component> findDownstreamRefs(String projectName, String refID);
	@Query("match (p:Project)--(n) where id(n)={0} return p")
	Project findProjectContaining(Long id);
	@Query("match (p:Project {name:{0}})--(n:Component {refID:{1}})-[r:DEPENDSON*1..]-(m:Component {refID:{2}}) return n limit 1")
	Component findAnyConnectionBetween(String projectName, String firstRefID, String secondRefID);
	@Query("match q=(p:Project)--(n:Component)-[r:DEPENDSON*1..]->(m:Component) where n.refID={1} AND p.name={0} return filter(n in nodes(q) where n:Component);")
	Set<Component> findAllReferencedNodes(String projectName, String refID);
}

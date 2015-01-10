package jrmds.user;

import jrmds.model.RegistredUser;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<RegistredUser, Long> {
	RegistredUser findByUsername(String username);
	RegistredUser findByemailAdress(String emailAdress);
	@Query("match (p:Project)--(r:RegistredUser) WHERE p.name={0} AND r.username={1} return r")
	RegistredUser findUser(String pName, String uName);
}
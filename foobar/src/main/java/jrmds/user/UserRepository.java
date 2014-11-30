package jrmds.user;

import jrmds.model.RegisteredUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<RegisteredUser, Long> {
	RegisteredUser findByUsername(String username);
	RegisteredUser findByemailAdress(String emailAdress);
}
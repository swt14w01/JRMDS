package jrmds.user;

import jrmds.model.RegistredUser;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<RegistredUser, Long> {
	RegistredUser findByUsername(String username);
	RegistredUser findByemailAdress(String emailAdress);
}
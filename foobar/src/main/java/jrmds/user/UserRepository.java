package jrmds.user;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jrmds.model.*;


@Repository
public interface UserRepository extends CrudRepository<RegisteredUser, Long> {
	RegisteredUser findByUsername(String username);
	RegisteredUser findByemailAdress(String emailAdress);
}
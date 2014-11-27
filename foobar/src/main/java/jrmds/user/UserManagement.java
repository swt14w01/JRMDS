package jrmds.user;

import jrmds.main.JrmdsManagement;
import jrmds.model.Project;
import jrmds.model.RegisteredUser;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UserManagement {
	@Autowired
	private GraphDatabaseService db;
	@Autowired
	private UserRepository UserRepository;
	@Autowired
	private JrmdsManagement controller;

	public RegisteredUser getUser(String username) {
		RegisteredUser temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = UserRepository.findByUsername(username);
			tx.success();
		}
		return temp;
	}

	public Boolean createUser(String username, String password,
			String emailAdress) {
		if (getUser(username) == null) {

			try (Transaction tx = db.beginTx()) {
				RegisteredUser temp = new RegisteredUser(username, password,
						emailAdress);
				UserRepository.save(temp);
				tx.success();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean userWorksOn(RegisteredUser registredUser, Project project) {
		boolean worksOn = registredUser.worksOn(project);
		if (worksOn)
			try (Transaction tx = db.beginTx()) {
				UserRepository.save(registredUser);

				tx.success();
			}
		return worksOn;
	}
}

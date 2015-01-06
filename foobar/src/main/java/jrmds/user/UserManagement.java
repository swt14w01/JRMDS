package jrmds.user;

import jrmds.model.Project;
import jrmds.model.RegistredUser;

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

	public RegistredUser getUser(String username) {
		RegistredUser temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = UserRepository.findByUsername(username);
			tx.success();
		}
		return temp;
	}
	
	public RegistredUser getEmailAdress(String emailAdress) {
		RegistredUser temp = null;
		try (Transaction tx = db.beginTx()) {
			temp = UserRepository.findByemailAdress(emailAdress);
			tx.success();
		}
		return temp;
	}

	public Boolean createUser(String username, String password,
			String emailAdress) {
		if (getUser(username) == null) {

			try (Transaction tx = db.beginTx()) {
				RegistredUser temp = new RegistredUser(username, password, emailAdress);
				UserRepository.save(temp);
				tx.success();
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean userWorksOn(RegistredUser registredUser, Project project) {
		boolean worksOn = registredUser.worksOn(project);
		if (worksOn)
			try (Transaction tx = db.beginTx()) {
				UserRepository.save(registredUser);

				tx.success();
			}
		return worksOn;
	}
	
	public void deleteRegistredUser(RegistredUser registredUser) {
		
		if (registredUser == null) throw new NullPointerException("Cannot delete NULL user");
		if (registredUser.getID() == null) throw new NullPointerException("Cannot delete user without ID");

		try (Transaction tx = db.beginTx()) {
			UserRepository.delete(registredUser.getID());
			if (UserRepository.findOne(registredUser.getID()) != null)
				throw new RuntimeException("Entity Project " + registredUser.getUsername() + " NOT deleted");
			tx.success();	
		}
	}
}
package jrmds.user;

import jrmds.model.Project;
import jrmds.model.RegistredUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class UserManagement {
	@Autowired
	private UserRepository UserRepository;

	@Transactional
	public RegistredUser getUser(String username) {
		RegistredUser temp = null;
			temp = UserRepository.findByUsername(username);
		return temp;
	}
	
	@Transactional
	public RegistredUser getEmailAdress(String emailAdress) {
		RegistredUser temp = null;
			temp = UserRepository.findByemailAdress(emailAdress);
		return temp;
	}

	@Transactional
	public Boolean createUser(String username, String password,
			String emailAdress) {
		if (getUser(username) == null) {

				RegistredUser temp = new RegistredUser(username, password, emailAdress);
				UserRepository.save(temp);
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean userWorksOn(RegistredUser registredUser, Project project) {
		boolean worksOn = registredUser.worksOn(project);
		if (worksOn)
				UserRepository.save(registredUser);
		return worksOn;
	}
	
	@Transactional
	public void deleteRegistredUser(RegistredUser registredUser) {
		
		if (registredUser == null) throw new NullPointerException("Cannot delete NULL user");
		if (registredUser.getID() == null) throw new NullPointerException("Cannot delete user without ID");

			UserRepository.delete(registredUser.getID());
			if (UserRepository.findOne(registredUser.getID()) != null)
				throw new RuntimeException("Entity Project " + registredUser.getUsername() + " NOT deleted");
	}
}
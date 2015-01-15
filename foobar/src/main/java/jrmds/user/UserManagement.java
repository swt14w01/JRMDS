package jrmds.user;

import jrmds.model.Project;
import jrmds.model.RegistredUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

/**
 * Methods to communicate with the User Database.
 * @author Robert Menger
 *
 */
@Controller
public class UserManagement {
	@Autowired
	private UserRepository UserRepository;

	/**
	 * Get a user from the database by username.
	 * @param username
	 * @return the loaded user.
	 */
	@Transactional
	public RegistredUser getUser(String username) {
		RegistredUser temp = null;
			temp = UserRepository.findByUsername(username);
		return temp;
	}
	
	/**
	 * Get a user from the database by email address.
	 * @param emailAdress
	 * @return the loaded user.
	 */
	@Transactional
	public RegistredUser getEmailAdress(String emailAdress) {
		RegistredUser temp = null;
			temp = UserRepository.findByemailAdress(emailAdress);
		return temp;
	}

	/**
	 * Create a new User with username, password an email address and save it in the database.
	 * @param username
	 * @param password
	 * @param emailAdress
	 * @return success.
	 */
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

	/**
	 * Add a reference between user and project.
	 * @param registredUser
	 * @param project
	 * @return success.
	 */
	@Transactional
	public boolean userWorksOn(RegistredUser registredUser, Project project) {
		boolean worksOn = registredUser.addProject(project);
		if (worksOn) UserRepository.save(registredUser);
		return worksOn;
	}
	
	/**
	 * checks if a given User is working on a project an so allowed for anything
	 * @param registredUser
	 * @param project
	 * @return true if the user works on that project
	 */
	@Transactional
	public Boolean workingOn(RegistredUser registredUser, Project project) {
		if (registredUser == null) return false;
		Boolean t = false;
		RegistredUser r = UserRepository.findUser(project.getName(), registredUser.getName());
		if (r != null) t = true;
		return t;
	}
	
	/**
	 * remove a reference to a project
	 * @param registredUser
	 * @param project
	 */
	@Transactional
	public void userNotWorksOn(RegistredUser registredUser, Project project) {
		RegistredUser r = getUser(registredUser.getName());
		if (r != null) {
			r.deleteProject(project);
			UserRepository.save(r);
		}
	}	
	
	/**
	 * Delete an existing user from the database.
	 * @param registredUser
	 */
	@Transactional
	public void deleteRegistredUser(RegistredUser registredUser) {
		
		if (registredUser == null) throw new NullPointerException("Cannot delete NULL user");
		if (registredUser.getID() == null) throw new NullPointerException("Cannot delete user without ID");

			UserRepository.delete(registredUser.getID());
			if (UserRepository.findOne(registredUser.getID()) != null)
				throw new RuntimeException("Entity Project " + registredUser.getUsername() + " NOT deleted");
	}
}

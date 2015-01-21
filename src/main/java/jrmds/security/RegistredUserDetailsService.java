package jrmds.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jrmds.model.RegistredUser;
import jrmds.user.UserManagement;

/**
 * Service to get Registred Users from the repository.
 * @author Leroy Buchholz
 *
 */
@Service
public class RegistredUserDetailsService implements UserDetailsService {
    
	@Autowired
	private UserManagement userManagement;

    /* (non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	RegistredUser user = userManagement.getUser(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }
        return new RegistredUserDetails(user);
    }

    /**
     * @author Leroy Buchholz
     *
     */
    private final static class RegistredUserDetails extends RegistredUser implements UserDetails {

    	private RegistredUserDetails() {}
        
    	private RegistredUserDetails(RegistredUser user) {
            super(user);
        }

        /* (non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities()
         */
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        /* (non-Javadoc)
         * @see jrmds.model.RegistredUser#getUsername()
         */
        @Override
        public String getUsername() {
            return getEmailAdress();
        }

        /* (non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired()
         */
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked()
         */
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetails#isCredentialsNonExpired()
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        /* (non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
         */
        @Override
        public boolean isEnabled() {
            return true;
        }

        private static final long serialVersionUID = 5639683223516504866L;
    }
}
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

@Service
public class RegistredUserDetailsService implements UserDetailsService {
    
	@Autowired
	private UserManagement userManagement;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
    	RegistredUser user = userManagement.getUser(username);
        if(user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }
        return new RegistredUserRepositoryUserDetails(user);
    }

    private final static class RegistredUserRepositoryUserDetails extends RegistredUser implements UserDetails {

        private RegistredUserRepositoryUserDetails(RegistredUser user) {
            super(user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        @Override
        public String getUsername() {
            return getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        private static final long serialVersionUID = 5639683223516504866L;
    }
}
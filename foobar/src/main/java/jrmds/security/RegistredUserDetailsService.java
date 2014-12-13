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
import jrmds.user.UserRepository;

@Service
public class RegistredUserDetailsService implements UserDetailsService {
    
    private UserRepository userRepository;
	
	@Autowired
	public RegistredUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		RegistredUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: "+ username);
        }
        return new RegistredUserDetails(user);
	}
	
	private final static class RegistredUserDetails extends RegistredUser implements UserDetails {
		
		private static final long serialVersionUID = 1L;

		private RegistredUserDetails(RegistredUser registredUser) {
		super(registredUser);
		}

		@Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        @Override
        public String getUsername() {
            return getEmailAdress();
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
	}
}

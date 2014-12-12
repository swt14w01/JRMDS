package jrmds.security;
/*
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
public class RegistredUserDetailService implements UserDetailsService {

	private final UserRepository users;

		@Autowired
		RegistredUserDetailService(UserRepository users)
		{
			this.users = users;
		}

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
		{
			RegistredUser user = users.findByUsername(username);
			if (user == null)
			{
				throw new UsernameNotFoundException("User not found: " + username);
			}

			return new UserRepositoryDetails(user);
		}

		private final static class UserRepositoryDetails extends RegistredUser implements UserDetails
		{
			private static final long	serialVersionUID	= 1L;
			
			public UserRepositoryDetails(RegistredUser user)
			{
				super(user);
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities()
			{
			return AuthorityUtils.createAuthorityList(roles.toArray(new String[0]));
			}

			@Override
			public boolean isAccountNonExpired()
			{
				return true;
			}

			@Override
			public boolean isAccountNonLocked()
			{
				// locking is not implemented
				return true;
			}

			@Override
			public boolean isCredentialsNonExpired()
			{
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public boolean isEnabled()
			{
				return true;
			}
		}
	}
	*/


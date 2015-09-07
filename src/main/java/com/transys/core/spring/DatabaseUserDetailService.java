package com.transys.core.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.transys.dao.admin.UserDAO;

public class DatabaseUserDetailService implements UserDetailsService {
	@Autowired
	private UserDAO userDAO;

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		com.transys.model.User user = userDAO
				.getUserByName(username);
		if (user != null) {
			List<GrantedAuthority> grantedAuthorties = new ArrayList<GrantedAuthority>();
			grantedAuthorties.add(new SimpleGrantedAuthority("ROLE_"
					+ user.getRole().getName().toUpperCase()));
			return new User(username, user.getPassword(),
					(user.getDeleteFlag() == 1), true, true, true,
					grantedAuthorties);
		} else {
			throw new UsernameNotFoundException("Invalid user");
		}
	}

}

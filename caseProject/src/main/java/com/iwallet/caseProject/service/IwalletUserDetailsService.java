package com.iwallet.caseProject.service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iwallet.caseProject.model.IwalletUser;
import com.iwallet.caseProject.model.Role;
import com.iwallet.caseProject.repository.IwalletUserRepository;

import jakarta.transaction.Transactional;

@Service
public class IwalletUserDetailsService  implements UserDetailsService {

	@Autowired
	private IwalletUserRepository userRepository;

	@Transactional
	public List<IwalletUser> getAllUsers() {
		List<IwalletUser> users = new ArrayList<IwalletUser>();
		users.addAll(userRepository.findAll());
		return users;
	}

	@Transactional
	public IwalletUser getUserById(Long id) {
		
		Optional<IwalletUser> userOpt = userRepository.findById(id);
		
		IwalletUser theUser = null;
		
		if(userOpt.isPresent())
			theUser = userOpt.get();
		
		return theUser;
	}

    @Autowired
    public IwalletUserDetailsService(IwalletUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        IwalletUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
package it.os.event.handler.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import it.os.event.handler.entity.UserETY;
import it.os.event.handler.exception.BusinessException;
import it.os.event.handler.exception.UserNotFoundException;
import it.os.event.handler.exception.UsernameAlreadyTakenException;
import it.os.event.handler.repository.IUserRepo;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {

	@Autowired
	private IUserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UserNotFoundException {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(String.format("User with username %s not found", username)));
	}

	public void signUpUser(UserETY user) {

		final Optional<UserETY> existingUser = userRepo.findByUsername(user.getUsername());

		if (existingUser.isPresent()) {
			throw new UsernameAlreadyTakenException("The username is already taken");
		}

		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
	}

    public List<UserDetails> getAllUsers() {
		
		List<UserDetails> details = new ArrayList<>();
		try {
			
			List<UserETY> users = userRepo.findAll();
			
			if (!CollectionUtils.isEmpty(users)) {
				for (UserETY user : users) {
					details.add(user);
				}
			}
		} catch (Exception e) {
			log.error("Error encountered while retrieving all users", e);
			throw new BusinessException("Error encountered while retrieving all users", e);
		}

		return details;
		
    }

    public void deleteUser(String username) {
		try {
			userRepo.deleteByUsername(username);
		} catch (Exception e) {
			log.error(String.format("Error encountered while deleting user with username %s", username), e);
			throw new BusinessException(String.format("Error encountered while deleting user with username %s", username), e);
		}
    }

}

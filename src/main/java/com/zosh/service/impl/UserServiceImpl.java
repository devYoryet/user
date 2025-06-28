package com.zosh.service.impl;

import com.zosh.exception.UserException;
import com.zosh.modal.User;
import com.zosh.repository.UserRepository;
import com.zosh.service.UserService;
import com.zosh.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Override
	public User getUserByEmail(String email) throws UserException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UserException("User not found with email: " + email);
		}
		return user;
	}

	@Override
	public User getUserFromJwtToken(String jwt) throws Exception {
		System.out.println("Getting user from JWT: " + jwt);

		// Remover "Bearer " del token si está presente
		if (jwt.startsWith("Bearer ")) {
			jwt = jwt.substring(7);
		}

		try {
			String email = jwtUtil.extractEmail(jwt);
			System.out.println("Extracted email: " + email);
			return getUserByEmail(email);
		} catch (Exception e) {
			System.out.println("Error extracting user from JWT: " + e.getMessage());
			throw new Exception("Invalid JWT token");
		}
	}

	@Override
	public User getUserById(Long id) throws UserException {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
}
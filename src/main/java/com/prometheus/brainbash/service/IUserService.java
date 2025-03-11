package com.prometheus.brainbash.service;

import com.prometheus.brainbash.exception.UserAlreadyExistsException;
import com.prometheus.brainbash.model.Role;
import com.prometheus.brainbash.model.User;

public interface IUserService {
	User createUser(String username, String password, Role role) throws UserAlreadyExistsException;
}

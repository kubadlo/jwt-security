package com.jakublesko.jwtsecurity.service;

import com.jakublesko.jwtsecurity.model.User;

import java.util.Optional;

public interface UserAccountService {

    Optional<User> getUserByToken(String token);

    String setRefreshToken(String username);
}

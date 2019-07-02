package com.jakublesko.jwtsecurity.service.impl;

import com.jakublesko.jwtsecurity.constants.SecurityConstants;
import com.jakublesko.jwtsecurity.model.RefreshToken;
import com.jakublesko.jwtsecurity.model.User;
import com.jakublesko.jwtsecurity.repository.RefreshTokenRepository;
import com.jakublesko.jwtsecurity.repository.UserRepository;
import com.jakublesko.jwtsecurity.service.UserAccountService;
import com.jakublesko.jwtsecurity.service.util.RandomUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final RefreshTokenRepository tokenRepository;

    private final UserRepository userRepository;

    public UserAccountServiceImpl(RefreshTokenRepository tokenRepository,
                                  UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserByToken(String token) {
        return tokenRepository
            .findByToken(token)
            .map(RefreshToken::getUser);
    }

    @Override
    public String setRefreshToken(String username) {
        return userRepository
            .findByUsernameIgnoreCase(username)
            .map(user -> {
                var token = new RefreshToken();
                token.setToken(RandomUtil.generateToken());
                token.setIssuedDate(Instant.now());
                token.setExpirationDate(Instant.now().plus(SecurityConstants.REFRESH_TOKEN_EXPIRATION, ChronoUnit.MILLIS));
                token.setUser(user);

                tokenRepository.save(token);
                return token.getToken();
            })
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}

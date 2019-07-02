package com.jakublesko.jwtsecurity.security;

import com.jakublesko.jwtsecurity.constants.SecurityConstants;
import com.jakublesko.jwtsecurity.service.UserAccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final UserAccountService userAccountService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   UserAccountService userAccountService) {
        this.authenticationManager = authenticationManager;
        this.userAccountService = userAccountService;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        var username = request.getParameter("username");
        var password = request.getParameter("password");
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((User) authentication.getPrincipal());

        var roles = user.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        var jwtToken = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
            .setHeaderParam("typ", SecurityConstants.JWT_TOKEN_TYPE)
            .setIssuer(SecurityConstants.JWT_TOKEN_ISSUER)
            .setAudience(SecurityConstants.JWT_TOKEN_AUDIENCE)
            .setSubject(user.getUsername())
            .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_TOKEN_EXPIRATION))
            .claim("rol", roles)
            .compact();

        var refreshToken = userAccountService.setRefreshToken(user.getUsername());

        response.addHeader(SecurityConstants.JWT_TOKEN_HEADER, SecurityConstants.JWT_TOKEN_PREFIX + jwtToken);
        response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER, refreshToken);
    }
}

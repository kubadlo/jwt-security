package com.jakublesko.jwtsecurity.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "token", length = 20, nullable = false, unique = true, updatable = false)
    private String token;

    @NotNull
    @Column(name = "issued_date", nullable = false, updatable = false)
    private Instant issuedDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false, updatable = false)
    private Instant expirationDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken)) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(token, that.token) &&
            Objects.equals(issuedDate, that.issuedDate) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, issuedDate, expirationDate, user);
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
            "id=" + id +
            ", issuedDate=" + issuedDate +
            ", expirationDate=" + expirationDate +
            ", user=" + user +
            '}';
    }
}

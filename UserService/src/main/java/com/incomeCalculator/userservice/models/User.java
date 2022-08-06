package com.incomeCalculator.userservice.models;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "USERS")
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @Column(unique = true)
    private String login;

    private String password;

    @ManyToOne
    private Role role;

    @OneToOne(orphanRemoval = true)
    private Token token;

    @OneToOne
    private Cookie cookie;

    private boolean isUsingCookie;

    @CreationTimestamp
    private LocalDateTime registrationDateTime;

    @UpdateTimestamp
    private LocalDateTime lastUpdateDateTime;

    public User() {
        isUsingCookie = false;
    }

    public User(String login, String password, Role role) {
        this.password = password;
        this.login = login;
        this.role = role;
        this.isUsingCookie = false;
    }

    public User(Long id, String login, String password, Role role) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.role = role;
        this.isUsingCookie = false;
    }

    public User(Long id, String login, String password, Role role, LocalDateTime registrationDateTime, LocalDateTime lastUpdateDateTime) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.isUsingCookie = false;
        this.registrationDateTime = registrationDateTime;
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String passwordHash) {
        this.password = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public LocalDateTime getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public void setCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public boolean isUsingCookie() {
        return isUsingCookie;
    }

    public void setUsingCookie(boolean usingCookie) {
        isUsingCookie = usingCookie;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", registrationDateTime=" + registrationDateTime +
                ", lastUpdateDateTime=" + lastUpdateDateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isUsingCookie == user.isUsingCookie && Objects.equals(id, user.id) && Objects.equals(login, user.login)
                && Objects.equals(password, user.password) && Objects.equals(role, user.role)
                && Objects.equals(token, user.token) && Objects.equals(cookie, user.cookie)
                && Objects.equals(registrationDateTime, user.registrationDateTime)
                && Objects.equals(lastUpdateDateTime, user.lastUpdateDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, token, cookie, isUsingCookie, registrationDateTime, lastUpdateDateTime);
    }
}
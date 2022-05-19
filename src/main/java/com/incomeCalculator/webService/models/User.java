package com.incomeCalculator.webService.models;

import javax.persistence.*;

@Entity(name = "USERS")
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @Column(unique = true)
    private String login;

    private String passwordHash;

    private String email;

    public User() {

    }

    public User(String login, String passwordHash, String email) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }
}

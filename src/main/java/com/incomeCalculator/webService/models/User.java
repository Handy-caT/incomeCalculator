package com.incomeCalculator.webService.models;

import javax.persistence.*;

@Entity(name = "USERS")
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @Column(unique = true)
    private String login;

    private String password;


    public User() {

    }

    public User(String login, String password, String email) {
        this.password = password;
        this.login = login;
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

}

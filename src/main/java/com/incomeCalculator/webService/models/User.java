package com.incomeCalculator.webService.models;

import javax.persistence.*;

@Entity(name = "USERS")
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    @Column(unique = true)
    private String login;

    private String password;

    @ManyToOne
    private Role role;


    public User() {

    }

    public User(String login, String password, Role role) {
        this.password = password;
        this.login = login;
        this.role = role;
    }

    public User(Long id,String login, String password, Role role) {
        this.id = id;
        this.password = password;
        this.login = login;
        this.role = role;
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

    @Override
    public String toString() {
        return "Ratio{" + "id=" + this.id +", login=" + this.login
                + ", passwordHash=" + this.password +", role=" + this.role + '}';
    }
}

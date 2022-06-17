package com.incomeCalculator.authapi.models;



import javax.persistence.*;

@Entity(name = "TOKENS")
public class Token {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

    @OneToOne
    private User user;

    private String token;

    public Token(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public Token(Long id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public Token() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

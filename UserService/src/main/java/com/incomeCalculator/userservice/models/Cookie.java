package com.incomeCalculator.userservice.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cookies")
public class Cookie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", nullable = false)
    String token;

    @OneToOne
    User user;

    public Cookie(Long id, String token, User user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public Cookie(User user) {
        this.user = user;
    }

    public Cookie() {
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cookie)) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(id, cookie.id) && Objects.equals(token, cookie.token) && Objects.equals(user, cookie.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, user);
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }

}
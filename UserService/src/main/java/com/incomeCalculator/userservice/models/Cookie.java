package com.incomeCalculator.userservice.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @CreationTimestamp
    private LocalDateTime registrationDateTime;

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

    public LocalDateTime getRegistrationDateTime() {
        return registrationDateTime;
    }

    public void setRegistrationDateTime(LocalDateTime registrationDateTime) {
        this.registrationDateTime = registrationDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cookie)) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(id, cookie.id) && Objects.equals(token, cookie.token)
                && Objects.equals(user, cookie.user)
                && Objects.equals(registrationDateTime, cookie.registrationDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, user, registrationDateTime);
    }

    @Override
    public String toString() {
        return "Cookie{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", registrationDateTime=" + registrationDateTime +
                '}';
    }
}
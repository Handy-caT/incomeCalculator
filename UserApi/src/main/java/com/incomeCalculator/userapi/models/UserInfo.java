package com.incomeCalculator.userapi.models;

import com.incomeCalculator.userservice.models.User;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "cookies_active")
    private boolean cookiesActive;

    @OneToOne
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isCookiesActive() {
        return cookiesActive;
    }

    public void setCookiesActive(boolean cookiesActive) {
        this.cookiesActive = cookiesActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return cookiesActive == userInfo.cookiesActive && Objects.equals(id, userInfo.id) && Objects.equals(email, userInfo.email) && Objects.equals(firstName, userInfo.firstName) && Objects.equals(lastName, userInfo.lastName) && Objects.equals(user, userInfo.user) && Objects.equals(createdAt, userInfo.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, cookiesActive, user, createdAt);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cookiesActive=" + cookiesActive +
                ", user=" + user.getId() +
                ", createdAt=" + createdAt +
                '}';
    }

}
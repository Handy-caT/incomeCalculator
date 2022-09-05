package com.incomeCalculator.userapi.requests;

import com.incomeCalculator.userapi.models.UserInfo;

import java.io.Serializable;
import java.util.Objects;

public class UserInfoDto implements Serializable {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final boolean cookiesActive;
    private final Long userId;

    public UserInfoDto(String email, String firstName, String lastName, boolean cookiesActive, Long userId) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cookiesActive = cookiesActive;
        this.userId = userId;
    }

    public UserInfoDto(UserInfo userInfo) {
        this.email = userInfo.getEmail();
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
        this.cookiesActive = userInfo.isCookiesActive();
        this.userId = userInfo.getUser().getId();
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean getCookiesActive() {
        return cookiesActive;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto entity = (UserInfoDto) o;
        return Objects.equals(this.email, entity.email) &&
                Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.cookiesActive, entity.cookiesActive) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, cookiesActive, userId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "email = " + email + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "cookiesActive = " + cookiesActive + ", " +
                "userId = " + userId + ")";
    }
}

package com.incomeCalculator.userservice.requests;

import java.io.Serializable;
import java.util.Objects;

public class CookieDto implements Serializable {
    private final String token;
    private final Long userId;

    public CookieDto(String token, Long userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookieDto entity = (CookieDto) o;
        return Objects.equals(this.token, entity.token) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, userId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "token = " + token + ", " +
                "userId = " + userId + ")";
    }
}

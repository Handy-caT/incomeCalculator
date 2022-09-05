package com.incomeCalculator.userservice.requests;

import java.io.Serializable;
import java.util.Objects;

public class TokenDto implements Serializable {
    private final Long userId;
    private final String token;

    public TokenDto(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDto entity = (TokenDto) o;
        return Objects.equals(this.userId, entity.userId) &&
                Objects.equals(this.token, entity.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, token);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "userId = " + userId + ", " +
                "token = " + token + ")";
    }
}

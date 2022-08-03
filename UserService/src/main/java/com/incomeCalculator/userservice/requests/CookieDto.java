package com.incomeCalculator.userservice.requests;

import java.io.Serializable;
import java.util.Objects;

public class CookieDto implements Serializable {
    private final String token;

    public CookieDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookieDto entity = (CookieDto) o;
        return Objects.equals(this.token, entity.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "token = " + token + ")";
    }

}

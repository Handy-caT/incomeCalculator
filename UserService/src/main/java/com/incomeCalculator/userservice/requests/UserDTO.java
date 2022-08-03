package com.incomeCalculator.userservice.requests;

import com.incomeCalculator.userservice.models.User;

public class UserDTO {

    Long id;
    String login;
    String roleName;

    public UserDTO(String login, String roleName, Long id) {
        this.login = login;
        this.roleName = roleName;
        this.id = id;
    }
    public UserDTO(User user) {
        this.login = user.getLogin();
        this.roleName = user.getRole().getRoleName();
        this.id = user.getId();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }


}

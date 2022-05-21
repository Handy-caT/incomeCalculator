package com.incomeCalculator.webService.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  Long id;

    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }
    public Role() {

    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String role) {
        this.roleName = role;
    }
}



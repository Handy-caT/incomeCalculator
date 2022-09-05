package com.incomeCalculator.userservice.repositories.user;


import com.incomeCalculator.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Override
    Optional<Role> findById(Long aLong);

    Optional<Role> findByRoleName(String roleName);

}

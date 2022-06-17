package com.incomeCalculator.authapi.repositories;


import com.incomeCalculator.authapi.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Override
    Optional<Role> findById(Long aLong);

    Optional<Role> findByRoleName(String roleName);

}

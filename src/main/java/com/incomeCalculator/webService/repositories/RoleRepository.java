package com.incomeCalculator.webService.repositories;

import com.incomeCalculator.webService.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Override
    Optional<Role> findById(Long aLong);

    Optional<Role> findByRoleName(String roleName);

}

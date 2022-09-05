package com.incomeCalculator.userapi;

import com.incomeCalculator.userservice.models.Role;
import com.incomeCalculator.userservice.repositories.user.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        List<Role> rolesList = new LinkedList<>();

        rolesList.add(new Role("ROLE_USER"));
        rolesList.add(new Role("ROLE_ADMIN"));
        rolesList.add(new Role("ROLE_SITE"));

        return args -> {
            for(Role role : rolesList) {
                if(!roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
                    log.info("Preloading " + roleRepository.save(role));
                }
            }
        };
    }


}
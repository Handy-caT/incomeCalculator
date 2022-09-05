package com.incomeCalculator.userapi.repositories;

import com.incomeCalculator.userapi.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUser_Id(Long id);

    Optional<UserInfo> findByEmail(String email);

}
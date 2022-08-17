package com.incomeCalculator.userapi.services;

import com.incomeCalculator.userapi.models.UserInfo;
import com.incomeCalculator.userapi.repositories.UserInfoRepository;
import com.incomeCalculator.userapi.requests.UserInfoDto;
import com.incomeCalculator.userservice.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserInfoService {

    private static final Logger log = LoggerFactory.getLogger(UserInfoService.class);

    private final UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    public UserInfo saveUserInfo(UserInfoDto userInfoDto, User user) {

        UserInfo userInfo = userInfoRepository.findByUser_Id(user.getId())
                .orElse(new UserInfo());

        userInfo.setFirstName(userInfoDto.getFirstName());
        userInfo.setLastName(userInfoDto.getLastName());
        userInfo.setEmail(userInfoDto.getEmail());
        userInfo.setCookiesActive(userInfoDto.getCookiesActive());
        userInfo.setUser(user);

        return userInfoRepository.save(userInfo);
    }
}

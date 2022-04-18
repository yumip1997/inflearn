package com.example.jwttutorial.user.service;

import com.example.jwttutorial.user.dto.UserDto;
import com.example.jwttutorial.user.entity.User;

public interface UserService {

    User signUp(UserDto userDto);
    User getUserInfoByUsername(String username);
    User getMyInfo();
    boolean existsByUsername(UserDto userDto);
}

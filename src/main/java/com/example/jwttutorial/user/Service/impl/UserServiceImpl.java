package com.example.jwttutorial.user.Service.impl;

import com.example.jwttutorial.com.exception.BusinessException;
import com.example.jwttutorial.com.security.util.SecurityUtil;
import com.example.jwttutorial.user.Service.UserService;
import com.example.jwttutorial.user.code.RoleCode;
import com.example.jwttutorial.user.dto.UserDto;
import com.example.jwttutorial.user.entity.Authority;
import com.example.jwttutorial.user.entity.User;
import com.example.jwttutorial.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.Security;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User signUp(UserDto userDto) {
        User alreadyExistUser = userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername());
        if(!ObjectUtils.isEmpty(alreadyExistUser)){
            throw new BusinessException("이미 가입되어 있는 User입니다 -> " + userDto.getNickname());
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(getRoleUserAuthority()))
                .build();

        return userRepository.save(user);
    }

    private Authority getRoleUserAuthority(){
        return Authority.builder().authorityName(RoleCode.ROLE_USER.getCode()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserInfoByUsername(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getMyInfo() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }
}

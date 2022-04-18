package com.example.jwttutorial.user.service.impl;

import com.example.jwttutorial.com.exception.custom.BusinessException;
import com.example.jwttutorial.com.security.util.SecurityUtil;
import com.example.jwttutorial.user.service.UserService;
import com.example.jwttutorial.user.com.code.RoleCode;
import com.example.jwttutorial.user.dto.UserDto;
import com.example.jwttutorial.user.entity.Authority;
import com.example.jwttutorial.user.entity.User;
import com.example.jwttutorial.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User signUp(UserDto userDto) {
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(getRoleUserAuthority()))
                .activated(true)
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

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(UserDto userDto) {
        return userRepository.existsByUsername(userDto.getUsername());
    }
}

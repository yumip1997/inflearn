package com.example.jwttutorial.login.service;

import com.example.jwttutorial.com.exception.BusinessException;
import com.example.jwttutorial.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.jwttutorial.user.entity.User user = userRepository.findOneWithAuthoritiesByUsername(username);
        return createUser(username, user);
    }

    private User createUser(String username, com.example.jwttutorial.user.entity.User user){
        validateUser(username, user);

        List<GrantedAuthority> grantedAuthorityList = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), grantedAuthorityList);
    }

    private void validateUser(String username, com.example.jwttutorial.user.entity.User user){
        if(ObjectUtils.isEmpty(user)){
            throw new BusinessException("User가 존재하지 않습니다. -> " + username);
        }
        if(!user.isActivated()){
            throw new BusinessException("비활성화 user 입니다! -> " + username);
        }
    }

}

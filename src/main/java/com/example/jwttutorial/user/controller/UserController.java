package com.example.jwttutorial.user.controller;

import com.example.jwttutorial.user.Service.UserService;
import com.example.jwttutorial.user.dto.UserDto;
import com.example.jwttutorial.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.signUp(userDto));
    }

    @PostMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfoByUsername(@RequestBody String username){
        return ResponseEntity.ok(userService.getUserInfoByUsername(username));
    };

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> getMyUserInfo(){
        return ResponseEntity.ok(userService.getMyInfo());
    }

}

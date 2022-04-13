package com.example.jwttutorial.user.dto;

import com.example.jwttutorial.user.utils.annotation.UserValid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserValid
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max =100)
    private String password;

    @NotNull
    @Size(min = 3, max =100)
    private String rePassword;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

}

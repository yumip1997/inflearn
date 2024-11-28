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
    @Size(min = 2, max = 50, message = "${username.minmax}")
    private String username;

    @NotNull
    @Size(min = 2, max =100, message = "${password.minmax}")
    private String password;

    @NotNull
    @Size(min = 2, max =100, message = "${password.minmax}")
    private String rePassword;

    @NotNull
    @Size(min = 2, max = 50, message = "${nickname.minmax}")
    private String nickname;

}

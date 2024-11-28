package com.example.jwttutorial.user.utils.validaton;

import com.example.jwttutorial.user.service.UserService;
import com.example.jwttutorial.user.utils.annotation.UserValid;
import com.example.jwttutorial.user.dto.UserDto;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<UserValid, UserDto> {

    private final UserService userService;

    @Override
    public boolean isValid(UserDto userDto, ConstraintValidatorContext constraintValidatorContext) {
        //비밀번호 일치 로직 수행
        if(isNotSamePassword(userDto)){
            addConstraintViolation(constraintValidatorContext, UserErrorMsg.NOT_SAME_PASSWORD);
            return false;
        }

        //중복확인 로직 수행
        if(userService.existsByUsername(userDto)){
            addConstraintViolation(constraintValidatorContext, UserErrorMsg.ALREADY_EXIST_USER);
            return false;
        }

        return true;
    }

    private boolean isNotSamePassword(UserDto userDto){
        return !userDto.getPassword().equals(userDto.getRePassword());
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMsg){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMsg)
                .addConstraintViolation();
    }
}

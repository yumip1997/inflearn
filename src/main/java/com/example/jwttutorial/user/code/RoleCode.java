package com.example.jwttutorial.user.code;

public enum RoleCode {

    ROLE_USER("ROLE_USER");

    private final String code;

    RoleCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

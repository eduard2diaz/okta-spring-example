package com.demo.okta_example.user;

public record UserRegisterDto(String email, String firstName, String lastName, String password, boolean active) {
}

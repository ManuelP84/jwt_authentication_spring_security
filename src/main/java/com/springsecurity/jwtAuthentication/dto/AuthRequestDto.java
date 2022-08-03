package com.springsecurity.jwtAuthentication.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Data
public class AuthRequestDto {

    @Email
    @Length(min = 6, max= 50)
    private String email;

    @Length(min = 4, max= 20)
    private String password;
}

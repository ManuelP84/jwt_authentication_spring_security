package com.springsecurity.jwtAuthentication.controller.user;

import com.springsecurity.jwtAuthentication.dto.AuthRequestDto;
import com.springsecurity.jwtAuthentication.dto.AuthResponseDto;
import com.springsecurity.jwtAuthentication.entity.user.User;
import com.springsecurity.jwtAuthentication.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto requestDto){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken( requestDto.getEmail(), requestDto.getPassword())
            );
            User user = (User) authentication.getPrincipal();

            String accessToken = jwtTokenUtil.generateAccessToken(user);  // JWT Token generated
            AuthResponseDto responseDto = new AuthResponseDto(user.getEmail(), accessToken);

            return ResponseEntity.ok(responseDto);
        }catch (BadCredentialsException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

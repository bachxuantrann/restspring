package com.springrest.restApp.controller;


import com.springrest.restApp.domain.User;
import com.springrest.restApp.domain.dto.LoginDTO;
import com.springrest.restApp.domain.dto.ResLoginDTO;
import com.springrest.restApp.service.UserService;
import com.springrest.restApp.util.SecurityUtil;
import com.springrest.restApp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${bxt.jwt.refresh-token-validity-in-seconds}")
    private long refeshTokenExpiration;
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil =  securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ApiMessage("user login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO){
//        get user from login
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
//        authenticate user
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        create token

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO res = new ResLoginDTO();
        User currentUser = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin= new ResLoginDTO.UserLogin(currentUser.getId(),currentUser.getEmail(),currentUser.getName());
            res.setUserLogin(userLogin);
        }
        String access_token =  this.securityUtil.createAccessToken(authentication, res.getUserLogin());
        res.setAccessToken(access_token);
//        create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(),res);
//        update token of user
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());
//        set cookies
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refeshTokenExpiration)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(res);
    }
    @GetMapping("/account")
    @ApiMessage("fetch account message")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount(){
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get():"";
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB!=null){
            userLogin.setId(currentUserDB.getId());
            userLogin.setEmail(currentUserDB.getEmail());
            userLogin.setName(currentUserDB.getName());
        }
        return ResponseEntity.ok().body(userLogin);
    }
}

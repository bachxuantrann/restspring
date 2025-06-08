package com.springrest.restApp.controller;

import com.springrest.restApp.domain.User;
import com.springrest.restApp.domain.dto.ResultPaginationDTO;
import com.springrest.restApp.service.UserService;
import com.springrest.restApp.util.annotation.ApiMessage;
import com.springrest.restApp.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user){
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        if(id>=1500) {
            throw new IdInvalidException("Id must be <1500");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("User with id " + id + " was deleted");
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        User user = this.userService.handleGetUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @GetMapping("/users")
    @ApiMessage("get all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable
            ){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUser(spec,pageable));
    }
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        User userUpdated = this.userService.handleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }
}

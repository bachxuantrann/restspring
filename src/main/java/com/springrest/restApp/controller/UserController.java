package com.springrest.restApp.controller;

import com.springrest.restApp.domain.User;
import com.springrest.restApp.domain.dto.ResCreateUserDTO;
import com.springrest.restApp.domain.dto.ResUpdateUserDTO;
import com.springrest.restApp.domain.dto.ResUserDTO;
import com.springrest.restApp.domain.dto.ResultPaginationDTO;
import com.springrest.restApp.service.UserService;
import com.springrest.restApp.util.annotation.ApiMessage;
import com.springrest.restApp.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
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
    @ApiMessage("create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws IdInvalidException{
        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if (isEmailExist){
            throw new IdInvalidException("Email: " + user.getEmail() + " already exists");
        }
        String hashPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(newUser));
    }
    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws IdInvalidException {
        User currentUser = this.userService.handleGetUser(id);
        if (currentUser == null){
            throw new IdInvalidException("User with id = "+id+" does not exist");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @GetMapping("/users/{id}")
    @ApiMessage("get user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") Long id) throws IdInvalidException {
        User user = this.userService.handleGetUser(id);
        if (user == null){
            throw new IdInvalidException("User with id = "+id+" does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(user));
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
    @ApiMessage("update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException{
        User userUpdated = this.userService.handleUpdateUser(user);
        if (userUpdated == null){
            throw new IdInvalidException("User with id = "+user.getId()+" does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(userUpdated));
    }
}

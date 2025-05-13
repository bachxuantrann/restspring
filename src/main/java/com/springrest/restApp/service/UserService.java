package com.springrest.restApp.service;

import com.springrest.restApp.domain.User;
import com.springrest.restApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User handleCreateUser(User user){
       return this.userRepository.save(user);

    }
    public void handleDeleteUser(Long id){
        this.userRepository.deleteById(id);
    }
    public User handleGetUser(Long id){
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;

    }
    public List<User> handleGetAllUser(){
        return this.userRepository.findAll();
    }
    public User handleUpdateUser(User user){
        User userUpdated = this.handleGetUser(user.getId());
        userUpdated.setName(user.getName());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setPassword(user.getPassword());
        return this.userRepository.save(userUpdated);
    }
}

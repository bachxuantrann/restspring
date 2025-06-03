package com.springrest.restApp.service;

import com.springrest.restApp.domain.User;
import com.springrest.restApp.domain.dto.MetaDTO;
import com.springrest.restApp.domain.dto.ResultPaginationDTO;
import com.springrest.restApp.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResultPaginationDTO handleGetAllUser(Pageable pageable){
        Page<User> users = this.userRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        MetaDTO mt = new MetaDTO();
        mt.setPage(users.getNumber()+1);
        mt.setPageSize(users.getSize());
        mt.setTotalPages(users.getTotalPages());
        mt.setTotal(users.getTotalElements());

        rs.setMetaDTO(mt);
        rs.setResult(users.getContent());
        return rs;
    }
    public User handleUpdateUser(User user){
        User userUpdated = this.handleGetUser(user.getId());
        userUpdated.setName(user.getName());
        userUpdated.setEmail(user.getEmail());
        userUpdated.setPassword(user.getPassword());
        return this.userRepository.save(userUpdated);
    }
    public User handleGetUserByUsername (String username){
        return this.userRepository.findByEmail(username);
    }
}

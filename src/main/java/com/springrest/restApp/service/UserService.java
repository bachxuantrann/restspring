package com.springrest.restApp.service;

import com.springrest.restApp.domain.User;
import com.springrest.restApp.domain.dto.*;
import com.springrest.restApp.repository.UserRepository;
import com.springrest.restApp.util.constant.GenderEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResultPaginationDTO handleGetAllUser(Specification<User> spec, Pageable pageable){
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        MetaDTO mt = new MetaDTO();
        mt.setPage(pageable.getPageNumber()+1);
        mt.setPageSize(pageable.getPageSize());
        mt.setTotalPages(users.getTotalPages());
        mt.setTotal(users.getTotalElements());
        rs.setMetaDTO(mt);
        List<ResUserDTO> listUser = users.getContent()
                        .stream().map(item -> new ResUserDTO(
                                item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()
                )).collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }
    public User handleUpdateUser(User user){
        User currentUser = this.handleGetUser(user.getId());
        if (currentUser!= null){
            currentUser.setAddress(user.getAddress());
            currentUser.setAge(user.getAge());
            currentUser.setAge(user.getAge());
            currentUser.setName(user.getName());

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User handleGetUserByUsername (String username){
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email){
        return this.userRepository.existsByEmail(email);
    }
    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        return res;
    }
    public ResUserDTO convertToResUserDTO(User user){
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }
}

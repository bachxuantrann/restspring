package com.springrest.restApp.repository;

import com.springrest.restApp.domain.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    boolean existsByEmail(@NotBlank(message = "email is required") String email);
}

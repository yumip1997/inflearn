package com.example.jwttutorial.user.repository;

import com.example.jwttutorial.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesByUsername(String username);

}

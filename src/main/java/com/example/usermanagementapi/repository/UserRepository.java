package com.example.usermanagementapi.repository;

import com.example.usermanagementapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // No need to explicitly declare save, findAll, findById, delete
    // JpaRepository already provides them all.
    Optional<User> findByEmail(String email);

}
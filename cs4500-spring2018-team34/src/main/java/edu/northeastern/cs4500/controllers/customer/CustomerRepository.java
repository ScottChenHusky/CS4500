package edu.northeastern.cs4500.controllers.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
}

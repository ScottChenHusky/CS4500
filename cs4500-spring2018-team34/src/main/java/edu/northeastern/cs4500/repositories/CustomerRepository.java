package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByUsername(String username);
    List<Customer> findByUsernameLike(String username);
    Customer findById(Integer id);
    List<Customer> findByIdIn(List<Integer> ids);
    boolean existsByUsername(String username);
}

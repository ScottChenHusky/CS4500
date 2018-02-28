package java.edu.northeastern.cs4500.controllers.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByUsernameAndPassword(String username, String password);
    List<Customer> findByUsernameLike(String username);
    Customer findById(Integer id);
    boolean existsByUsername(String username);
}

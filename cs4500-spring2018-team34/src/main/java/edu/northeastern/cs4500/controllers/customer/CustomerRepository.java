package edu.northeastern.cs4500.controllers.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<CustomerObject, Integer> {
    @Procedure(name = "custom_info")
    List<CustomerObject> customer_info(@Param("username") String inParam1);
}

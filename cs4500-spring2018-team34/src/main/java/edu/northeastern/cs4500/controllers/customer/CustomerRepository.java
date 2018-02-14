package edu.northeastern.cs4500.controllers.customer;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<CustomerObject, Integer> {
    @Procedure(name = "custom_info")
    String customer_info(@Param("username") String inParam1);
}

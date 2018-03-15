package edu.northeastern.cs4500.controllers.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerFollowingRepository extends JpaRepository<CustomerFollowing, Integer> {
    List<CustomerFollowing> findByCustomerFromId(Integer customerFromId);
    List<CustomerFollowing> findByCustomerToId(Integer customerToId);
    List<CustomerFollowing> findByCustomerFromIdAndCustomerToId(Integer customerFromId, Integer customerToId);
}

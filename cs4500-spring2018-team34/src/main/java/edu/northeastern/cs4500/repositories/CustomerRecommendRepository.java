package edu.northeastern.cs4500.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRecommendRepository extends JpaRepository<CustomerRecommend, Integer> {
    boolean existsByCustomerFromAndCustomerToAndMovieId(Integer customerFrom, Integer customerTo, Integer movieId);
    List<CustomerRecommend> findAllByCustomerTo(Integer customerTo);
}

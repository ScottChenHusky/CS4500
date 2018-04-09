package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 4/8/18.

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRecommendRepository extends JpaRepository<MovieRecommend, Integer> {
  List<MovieRecommend> findTop2ByCustomerIdOrderByWeightsDesc(int customerId);

  boolean existsMovieRecommendByCustomerIdAndAndTag(int customerId, String tag);

  MovieRecommend findMovieRecommendByCustomerIdAndTag(int customerId, String tag);

}

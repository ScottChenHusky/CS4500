package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 3/6/18.

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCommentRepository extends JpaRepository<MovieComment, Integer> {
  List<MovieComment> findMovieCommentsByCustomerId(Integer customerId);
  List<MovieComment> findMovieCommentByMovieIdOrderByDate(Integer movieId);
  boolean existsMovieCommentByCustomerIdAndMovieId(Integer customerId, Integer movieId);
  void deleteMovieCommentByCustomerIdAndMovieId(Integer customerId, Integer movieId);


}

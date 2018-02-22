package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
  List<Movie> findByName(String name);
  Movie findById(Integer id);
  List<Movie> findByLanguageLike(String language);
  List<Movie> findByActorsLike(String actors);
  List<Movie> findByNameLike(String searchby);
  List<Movie> findByCountryLike(String country);
}

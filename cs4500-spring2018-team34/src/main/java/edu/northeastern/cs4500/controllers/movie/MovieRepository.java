package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import javax.transaction.Transactional;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
  List<Movie> findByName(String name);

  Movie findById(Integer id);

  List<Movie> findByLanguageContaining(String language);

  List<Movie> findByActorsContaining(String actors);

  List<Movie> findByNameContaining(String name);

  List<Movie> findByCountryContaining(String country);

  List<Movie> findByOmdbreference(String omdbreference);

  List<Movie> findTop5ByOrderByIdDesc();

  List<Movie> findTop5ByOrderByScoreDesc();

  List<Movie> findByIdIn(List<Integer> ids);

  boolean existsById(Integer Id);

  boolean existsByOmdbreference(String omdbreference);


  long count();

  @Transactional
  Long deleteById(Integer Id);

}

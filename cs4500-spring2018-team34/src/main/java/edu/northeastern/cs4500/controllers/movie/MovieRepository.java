package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
  List<Movie> findByName(String name);
  Movie findById(Integer id);
  List<Movie> findByLanguageContaining(String language);
  List<Movie> findByActorsContaining(String actors);
  List<Movie> findByNameContaining(String searchby);
  List<Movie> findByCountryContaining(String country);
}

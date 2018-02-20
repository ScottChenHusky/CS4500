package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/20/18.

import java.util.List;

public class SearchMovieResponseJSON {
  private List<Movie> movies;
  private String message;

  public SearchMovieResponseJSON withMovie(List<Movie> input){
    this.movies = input;
    return this;
  }
  public SearchMovieResponseJSON withMessage(String message){
    this.message = message;
    return this;
  }


}

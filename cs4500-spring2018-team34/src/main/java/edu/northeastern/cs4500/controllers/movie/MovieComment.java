package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 3/6/18.


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie_comment")
public class MovieComment {
  @Id
  @GeneratedValue
  public Integer id;
  private String review;
  private String score;
  private Date date;
  private int customerId;
  private int movieId;

  public MovieComment() {

  }

  public MovieComment(String review, String score, Date date, Integer customerId, Integer movieId) {
    this.review = review;
    this.score = score;
    this.date = date;
    this.customerId = customerId;
    this.movieId = movieId;

  }

  public void setReview(String review) {
    this.review = review;
  }

  public void setScore(String score) {
    this.score = score;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  public String getReview() {
    return review;
  }

  public String getScore() {
    return score;
  }

  public Date getDate() {
    return date;
  }

  public int getCustomerId() {
    return customerId;
  }

  public int getMovieId() {
    return movieId;
  }


  public Map<String, String> toMap() {
    Map<String, String> map = new HashMap<>();

    Field[] fields = this.getClass().getDeclaredFields();

    for (Field field : fields) {
      try {
        map.put(field.getName(), field.get(this).toString());
      } catch (Exception ignored) {
      }
    }
    return map;

  }

}

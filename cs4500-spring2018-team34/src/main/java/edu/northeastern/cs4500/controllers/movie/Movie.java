package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {
  @Id
  private Integer Id;
  private String name;
  private Date date;
  private Integer score;
  private String description;
  private String level;
  private String Language;
  private Integer time;
  private String omdbreference;
  private String rtreference;
  private String tmdbreference;

  public void setName(String name) {
    this.name = name;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public void setLanguage(String language) {
    Language = language;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public void setOmdbreference(String omdbreference){
    this.omdbreference = omdbreference;
  }

  public void setRtreference(String rtreference){ this.rtreference = rtreference; }

  public void setTmdbreference(String tm){ this.tmdbreference = tmdbreference; }
  public Integer getId() {
    return Id;
  }

  public String getName() {
    return name;
  }

  public Date getDate() {
    return date;
  }

  public Integer getScore() {
    return score;
  }

  public String getDescription() {
    return description;
  }

  public String getLevel() {
    return level;
  }

  public String getLanguage() {
    return Language;
  }

  public Integer getTime() {
    return time;
  }
}

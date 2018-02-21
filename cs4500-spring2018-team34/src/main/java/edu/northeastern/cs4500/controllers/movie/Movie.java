package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {
  @Id
  @GeneratedValue
  private Integer Id;
  private String name;
  private String date;
  private Float score;
  private String description;
  private String level;
  private String Language;
  private String time;
  private String omdbreference;
  private String rtreference;
  private String tmdbreference;

  public void setName(String name) {
    this.name = name;
  }
  public Movie withName(String name) {
    this.setName(name);
    return this;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public Movie withDate(String date){
    this.setDate(date);
    return this;
  }
  public void setScore(Float score) {
    this.score = score;
  }
  public Movie withScore(Float score){
    this.setScore(score);
    return this;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public Movie withDescription(String description){
    this.setDescription(description);
    return this;
  }
  public void setLevel(String level) {
    this.level = level;
  }
  public Movie withLevel(String level) {
    this.setLevel(level);
    return this;
  }
  public void setLanguage(String language) {
    Language = language;
  }
  public Movie withLanguage(String language){
    this.setLanguage(language);
    return this;
  }
  public void setTime(String time) {
    this.time = time;
  }
  public Movie withTime(String time){
    this.setTime(time);
    return this;
  }
  public void setOmdbreference(String omdbreference){
    this.omdbreference = omdbreference;
  }
  public Movie withOmdbreference(String omdbreference){
    this.setOmdbreference(omdbreference);
    return this;
  }
  public void setRtreference(String rtreference){ this.rtreference = rtreference; }
  public Movie withRtreference(String rtreference){
    this.setRtreference(rtreference);
    return this;
  }
  public void setTmdbreference(String tm){ this.tmdbreference = tmdbreference; }
  public Movie withTmdbreference(String tm){
    this.setTmdbreference(tm);
    return this;
  }
  public Integer getId() {
    return Id;
  }

  public String getName() {
    return name;
  }

  public String getDate() {
    return date;
  }

  public Float getScore() {
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

  public String getTime() {
    return time;
  }
}

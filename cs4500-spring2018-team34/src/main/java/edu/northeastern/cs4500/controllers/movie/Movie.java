package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie")
public class Movie {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  private String date;
  private Float score;
  private String description;
  private String level;
  private String language;
  private String time;
  private String omdbreference;
  private String rtreference;
  private String tmdbreference;
  private String director;
  private String actors;
  private String country;
  private String awards;
  private String poster;
  private String boxoffice;

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
  public void setlanguage(String language) {
    language = language;
  }
  public Movie withLanguage(String language){
    this.setlanguage(language);
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
  public void setDirector(String director){this.director = director;}
  public Movie withDirector(String director){
    this.setDirector(director);
    return this;
  }

  public void setActors(String actors){ this.actors = actors;}
  public Movie withActors(String actors){
    this.setActors(actors);
    return this;
  }

  public void setCountry(String country) {this.country = country; }
  public Movie withCountry(String country) {
    this.country = country;
    return this;
  }

  public void setAwards(String awards){ this.awards = awards; }
  public Movie withAwards(String awards){
    this.setAwards(awards);
    return this;
  }

  public void setPoster(String poster) {this.poster = poster;}
  public Movie withPoster(String poster) {
    this.setPoster(poster);
    return this;
  }

  public void setBoxoffice(String boxoffice){this.boxoffice = boxoffice;}
  public Movie withBoxOffice(String boxoffice){
    this.setBoxoffice(boxoffice);
    return this;
  }

  public Integer getId() {
    return this.id;
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

  public String getlanguage() {
    return language;
  }

  public String getTime() {
    return time;
  }

  public String getDirector() {return director; }

  public String getActors(){ return actors; }

  public String getCountry(){
    return this.country;
  }

  public String getAwards(){return this.awards;}

  public String getPoster(){return this.poster;}

  public String getBoxoffice(){return this.boxoffice;}
  public Map<String, String> toMap(){
    Map<String, String> map = new HashMap<>();
    Field[] fields = this.getClass().getFields();
    for(int i = 0; i < fields.length; i++){
      try {
        map.put(fields[i].getName(), (String) fields[i].get(this));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    map.remove("id");
    return map;

  }
  @Override
  public boolean equals(Object obj) {
    return obj instanceof Movie && this.id == ((Movie) obj).id;
  }
}

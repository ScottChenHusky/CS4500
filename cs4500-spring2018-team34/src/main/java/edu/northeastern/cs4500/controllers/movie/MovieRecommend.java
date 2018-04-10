package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 4/8/18.


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie_recommend")
public class MovieRecommend {
  @Id
  @GeneratedValue
  public Integer id;
  private int weights;
  private int customerId;
  private String tag;

  public MovieRecommend(){}

  public MovieRecommend(int weights, int customerId, String tag){
    this.weights = weights;
    this.customerId = customerId;
    this.tag = tag;
  }

  public MovieRecommend withWeights(int weights){
    this.setWeights(weights);
    return this;
  }

  public MovieRecommend withCustomerId(int customerId){
    this.setCustomerId(customerId);
    return this;
  }

  public MovieRecommend withTag(String tag){
    this.setTag(tag);
    return this;
  }

  public int getWeights() {
    return weights;
  }

  public void setWeights(int weights) {
    this.weights = weights;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}

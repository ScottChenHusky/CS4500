package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/23/18.

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MovieTest {
  @Test
  public void testEquals1(){
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 1;
    assertTrue(movie1.equals(movie2));
  }

  @Test
  public void testEquals2(){
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 2;
    assertTrue(!movie1.equals(movie2));
  }

  @Test
  public void testEquals3(){
    Movie movie1 = new Movie();
    movie1.id = 1;
    assertTrue(!movie1.equals("movie2"));
  }

  @Test
  public void testHashCode1(){
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 1;
    assertTrue(movie1.hashCode() == movie2.hashCode());
  }

  @Test
  public void testHashCode2(){
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 2;
    assertTrue(movie1.hashCode() != movie2.hashCode());
  }
}

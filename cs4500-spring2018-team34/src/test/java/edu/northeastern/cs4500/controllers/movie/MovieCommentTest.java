package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 3/14/18.

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieCommentTest {

  @Test
  public void testMovieComment(){
    MovieComment mc = new MovieComment("A","0", new Date(), 1, 2);
    assertEquals("A", mc.getReview());
    assertEquals("0", mc.getScore());

  }
  @Test
  public void testReview(){
    MovieComment mc = new MovieComment();
    mc.setReview("Review");
    String re = mc.getReview();
    assertEquals("Review", mc.getReview());
    assertEquals(re, mc.getReview());

  }

  @Test
  public void testScore(){
    MovieComment mc = new MovieComment();
    mc.setScore("3");
    String re = mc.getScore();
    assertEquals("3", mc.getScore());
    assertEquals(re, mc.getScore());

  }

  @Test
  public void testDate(){
    MovieComment mc = new MovieComment();
    Date date = new Date();
    mc.setDate(date);
    Date d = mc.getDate();
    assertEquals(date, mc.getDate());
    assertEquals(d, mc.getDate());

  }

  @Test
  public void testCustomerId() {
    MovieComment mc = new MovieComment();
    mc.setCustomerId(1);
    int customer_id = mc.getCustomerId();
    assertEquals(1, mc.getCustomerId());
    assertEquals(1, customer_id);
  }

  @Test
  public void testMovieId() {
    MovieComment mc = new MovieComment();
    mc.setMovieId(1);
    int movie_id = mc.getMovieId();
    assertEquals(1, mc.getMovieId());
    assertEquals(1, movie_id);
  }

  @Test
  public void testToMap() {
    MovieComment m1 = new MovieComment();
    m1.setReview("A");
    m1.setScore("0");
    Map mm = m1.toMap();
    assertEquals("A", mm.get("review"));
    assertEquals("0", mm.get("score"));
  }

}

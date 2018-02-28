package java.edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/23/18.

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
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

  @Test
  public void testName(){
    Movie movie1 = new Movie();
    movie1.setName("Test");
    assertEquals("Test", movie1.getName());
  }

  @Test
  public void testDate(){
    Movie movie1 = new Movie();
    movie1.setDate("1999-09-09");
    assertEquals( "1999-09-09", movie1.getDate());
  }

  @Test
  public void testScore(){
    Movie movie1 = new Movie();
    movie1.setScore("1.1");
    assertEquals("1.1", movie1.getScore());
  }

  @Test
  public void testDescription(){
    Movie movie1 = new Movie();
    movie1.setDescription("Good");
    assertEquals("Good", movie1.getDescription());
  }

  @Test
  public void testLevel(){
    Movie movie1 = new Movie();
    movie1.setLevel("PG-13");
    assertEquals("PG-13", movie1.getLevel());
  }
}
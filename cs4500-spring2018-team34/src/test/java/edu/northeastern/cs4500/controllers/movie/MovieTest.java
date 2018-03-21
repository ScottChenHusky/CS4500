package edu.northeastern.cs4500.controllers.movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MovieTest {
  @Test
  public void testEquals1() {
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 1;
    assertTrue(movie1.equals(movie2));
  }

  @Test
  public void testEquals2() {
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 2;
    assertTrue(!movie1.equals(movie2));
  }

  @Test
  public void testEquals3() {
    Movie movie1 = new Movie();
    movie1.id = 1;
    assertTrue(!movie1.equals("movie2"));
  }

  @Test
  public void testHashCode1() {
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 1;
    assertEquals(movie1.hashCode(), movie2.hashCode());
  }

  @Test
  public void testHashCode2() {
    Movie movie1 = new Movie();
    movie1.id = 1;
    Movie movie2 = new Movie();
    movie2.id = 2;
    assertTrue(movie1.hashCode() != movie2.hashCode());
  }

  @Test
  public void testName() {
    Movie movie1 = new Movie();
    movie1.setName("Test");
    assertEquals("Test", movie1.getName());
  }

  @Test
  public void testDate() {
    Movie movie1 = new Movie();
    movie1.setDate("1999-09-09");
    assertEquals("1999-09-09", movie1.getDate());
  }

  @Test
  public void testScore() {
    Movie movie1 = new Movie();
    movie1.setScore("1.1");
    assertEquals("1.1", movie1.getScore());
  }

  @Test
  public void testDescription() {
    Movie movie1 = new Movie();
    movie1.setDescription("Good");
    assertEquals("Good", movie1.getDescription());
  }

  @Test
  public void testLevel() {
    Movie movie1 = new Movie();
    movie1.setLevel("PG-13");
    assertEquals("PG-13", movie1.getLevel());
  }

  @Test
  public void testWith() {
    Movie m = new Movie();
    m.withName("A");
    assertEquals("A", m.getName());
    m.withDate("1999-09-09");
    assertEquals("1999-09-09", m.getDate());
    m.withScore("3.3");
    assertEquals("3.3", m.getScore());
    m.withDescription("A Good Movie");
    assertEquals("A Good Movie", m.getDescription());
    m.withLevel("PG-13");
    assertEquals("PG-13", m.getLevel());
    m.withLanguage("Chinese");
    assertEquals("Chinese", m.getlanguage());
    m.withTime("123");
    assertEquals("123", m.getTime());
    m.withOmdbreference("OMDB");
    assertEquals("OMDB", m.getOmdbreference());
    m.withRtreference("RT");
    assertEquals("RT", m.getRtreference());
    m.withTmdbreference("TMDB");
    assertEquals("TMDB", m.getTmdbreference());
    m.withDirector("L");
    assertEquals("L", m.getDirector());
    m.withActors("L");
    assertEquals("L", m.getActors());
    m.withCountry("China");
    assertEquals("China", m.getCountry());
    m.withAwards("Best");
    assertEquals("Best", m.getAwards());
    m.withPoster("Poster");
    assertEquals("Poster", m.getPoster());
    m.withBoxOffice("Box");
    assertEquals("Box", m.getBoxoffice());

  }


  @Test
  public void testEquals() {
    Movie m1 = new Movie();
    Movie m2 = new Movie();
    m1.setDirector("123");
    m2.setDirector("123");
    assertTrue(m1.equals(m2));
  }

  @Test
  public void testToMap() {
    Movie m1 = new Movie();
    m1.withName("A")
            .withAwards("B");
    Map mm = m1.toMap();
    assertEquals("A", mm.get("name"));
    assertEquals("B", mm.get("awards"));
  }
}


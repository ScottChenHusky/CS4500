package edu.northeastern.cs4500.controllers.movie;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoiveControllerTest {

	@Test
	public void hashCodeTest(){
	    Movie m = new Movie();
	    int i = m.hashCode();
	    assertTrue(i == 0);
	}
	
	@Test
	public void equals() {
		Movie m1 = new Movie();
		Movie m2 = new Movie();
		m1.setDirector("123");
		m2.setDirector("123");
		assertTrue(m1.equals(m2));
	}

}

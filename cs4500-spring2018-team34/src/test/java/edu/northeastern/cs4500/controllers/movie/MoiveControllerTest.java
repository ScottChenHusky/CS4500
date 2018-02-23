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

}

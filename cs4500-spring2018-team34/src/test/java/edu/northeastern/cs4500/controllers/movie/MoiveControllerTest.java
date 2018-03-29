package edu.northeastern.cs4500.controllers.movie;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.northeastern.cs4500.repositories.CustomerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)

public class MoiveControllerTest {
	
	MovieRepository mR;
	MovieRepository mR1;
	CustomerRepository cR;
	MovieCommentRepository mCR;
	
	
	@Before
	public void setup() {
		mR = mock(MovieRepository.class);
		List lN = new ArrayList<Movie>();
		lN.add(new Movie());
		when(mR.findByName("test")).thenReturn(lN);
		when(mR.findByNameContaining("test")).thenReturn(lN);
		when(mR.findByLanguageContaining("test")).thenReturn(lN);
		when(mR.findByActorsContaining("test")).thenReturn(lN);
		when(mR.findByCountryContaining("test")).thenReturn(lN);
		when(mR.findById(1)).thenReturn(new Movie());
		when(mR.existsById(1)).thenReturn(true);
		
		mR1 = mock(MovieRepository.class);
		List lN1 = new ArrayList<Movie>();
		when(mR1.findByName("t")).thenReturn(lN1);
		when(mR1.findByNameContaining("t")).thenReturn(lN1);
		when(mR1.findByLanguageContaining("t")).thenReturn(lN1);
		when(mR1.findByActorsContaining("t")).thenReturn(lN1);
		when(mR1.findByCountryContaining("t")).thenReturn(lN1);
		
		cR = mock(CustomerRepository.class);
		
		mCR = mock(MovieCommentRepository.class);
		MovieComment com = new MovieComment();
		when(mCR.getOne(1)).thenReturn(com);
		when(mCR.existsMovieCommentByCustomerIdAndMovieId(1, 1)).thenReturn(true);
	}
	
	@Test
	public void testCreateMap(){
		MovieController mc = new MovieController();
		Movie m = new Movie();
		List l1 = new ArrayList();
		l1.add(m);
		JSONArray map = new JSONArray();
		map.add(new JSONObject(m.toMap()));
		assertEquals(map, mc.createMap(l1));
		
	}
	
	@Test
	public void testSearchMovies() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/search/").param("name", "test")).andExpect(status().isOk());
		
		//assertEquals(ResponseEntity.ok().body(new JSONObject()), 
		//		mock.perform(get("api/movie/search?name=test")).andExpect(status().isOk()));
	}
	
	/*
	@Test
	public void testSearchMovies1() throws Exception{
		MovieController mc = new MovieController(mR1, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/search/").param("name", "t")).andExpect(status().isOk());
		
	}*/
	
	@Test
	public void testGetMovie() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/get/").param("id", "1")).andExpect(status().isOk());
		//assertEquals(ResponseEntity.ok().body(new JSONObject()), 
		//		mock.perform(get("api/movie/search?name=test")).andExpect(status().isOk()));
	}
	
	@Test
	public void testAddComment() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("customerId", "1");
		json.put("movieId", "1");
		json.put("review", "test");
		json.put("score", "3");
		
		mock.perform(post("/api/movie/addComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
		
		JSONObject json1 = new JSONObject();
		json1.put("customerId", "0");
		json1.put("movieId", "0");
		json1.put("review", "test");
		json1.put("score", "3");
		
		mock.perform(post("/api/movie/addComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json1.toJSONString())
				).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteComment() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("customerId", "1");
		json.put("movieId", "1");
		
		mock.perform(post("/api/movie/deleteComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
		
		JSONObject json1 = new JSONObject();
		json1.put("customerId", "0");
		json1.put("movieId", "0");
		mock.perform(post("/api/movie/deleteComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json1.toJSONString())
				).andExpect(status().isOk());
	}
	
	@Test
	public void testUpdateComment() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("id", "1");
		json.put("review", "test");
		json.put("score", "3");
		
		mock.perform(post("/api/movie/updateComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteMovie() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("movieId", "1");
		json.put("loggedInUserId", "1");
		
		mock.perform(post("/api/deleteMovie/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
	}
	
	/*
	@Test
	public void testMainSearch() {
		MovieController mc = new MovieController(mR, cR, mCR);
		JSONObject item = new JSONObject();
		JSONObject results = new JSONObject();
		item.put("date","2013-06-29");
		item.put("country", "USA");
		item.put("tmdbreference", "");
		item.put("level", "TV-MA");
		item.put("director", "Chris Mason Johnson");
		item.put("description", "San Francisco, 1985. Two opposites attract "
					+ "at a modern dance company. Together, their courage and resilience are tested as "
					+ "they navigate a world full of risks and promise, against the backdrop of a disease "
					+ "no one seems to know anything about.");
		item.put("$jacocoData", "");
		item.put("language", "en");
		item.put("omdbreference", "tt2407380");
		item.put("boxoffice", "N/A");
		item.put("score", "7.1");
		item.put("actors", "Scott Marlowe, Matthew Risch, Evan Boomer, Kevin Clarke");
		item.put("awards", "3 wins & 3 nominations.");
		item.put("name", "Test");
		item.put("time", "89");
		item.put("poster", "https://ia.media-imdb.com/images/M/"
				+ "MV5BMTQwMDU5NDkxNF5BMl5BanBnXkFtZTcwMjk5OTk4OQ@@._V1_SX300.jpg");
		item.put("t1", "6T8Yqh73vXs");
		item.put("t2", "2lvJYjph874");
		item.put("t3", "");
		results.put("Results", item);
		
		assertEquals(false, mc.mainSearch("Captain America: Civil War").isEmpty());
		
	}*/
}



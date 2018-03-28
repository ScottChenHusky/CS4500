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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import edu.northeastern.cs4500.repositories.CustomerRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)

public class MoiveControllerTest {
	
	MovieRepository mR;
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
		
		cR = mock(CustomerRepository.class);
		
		mCR = mock(MovieCommentRepository.class);
	}
	
	@Test
	public void testCreateMap(){
		MovieController mc = new MovieController();
		Movie m = new Movie();
		List l1 = new ArrayList();
		l1.add(m);
		Map<String, JSONObject> map = new HashMap();
		map.put("type0", new JSONObject(m.toMap()));
		assertEquals(map, mc.createMap("type", l1, "Movie"));
		MovieComment mco = new MovieComment();
		l1 = new ArrayList();
		l1.add(mco);
		map = new HashMap();
		map.put("type0", new JSONObject(mco.toMap()));
		assertEquals(map, mc.createMap("type", l1, "Comment"));
	}
	
	@Test
	public void testSearchMovies() throws Exception{
		MovieController mc = new MovieController();
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		//mock.perform(get("/api/movie/search/{name}", "test"));
		assertEquals(ResponseEntity.ok().body(new JSONObject()), 
				mock.perform(get("api/movie/search?name=test")).andExpect(status().isOk()));
	}
	
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
		
		assertEquals(false, mc.mainSearch("test").isEmpty());
		/*
		 * {"Results":{"date":"2013-06-29","country":"USA","tmdbreference":"","level":"TV-MA",
			"director":"Chris Mason Johnson", "description":"San Francisco, 1985. Two opposites attract "
					+ "at a modern dance company. Together, their courage and resilience are tested as "
					+ "they navigate a world full of risks and promise, against the backdrop of a disease "
					+ "no one seems to know anything about.","language":"en","omdbreference":"tt2407380",
					"boxoffice":"N\/A","score":"7.1","actors":"Scott Marlowe, Matthew Risch, Evan Boomer, "
							+ "Kevin Clarke","awards":"3 wins & 3 nominations.","name":"Test","time":"89",
							"poster":"https:\/\/ia.media-imdb.com\/images\/M\/MV5BMTQwMDU5NDkxNF5BMl5BanBnX"
									+ "kFtZTcwMjk5OTk4OQ@@._V1_SX300.jpg","t1":"6T8Yqh73vXs","t2":"2lvJYjph874","t3":""}}
		 */
	}
}



package edu.northeastern.cs4500.controllers.movie;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.cs4500.controllers.csv.CsvApi;
import edu.northeastern.cs4500.repositories.Customer;
import edu.northeastern.cs4500.repositories.CustomerRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)

public class MoiveControllerTest {
	
	MovieRepository mR;
	//MovieRepository mR1;
	CustomerRepository cR;
	MovieCommentRepository mCR;
	CsvApi cA;
	MovieRecommendRepository mRR;
	
	@Before
	public void setup() throws Exception {
		mR = mock(MovieRepository.class);
		List lN = new ArrayList<Movie>();
		lN.add(new Movie());
		when(mR.findByName("test")).thenReturn(lN);
		when(mR.findByNameContaining("test")).thenReturn(lN);
		when(mR.findByLanguageContaining("test")).thenReturn(lN);
		when(mR.findByActorsContaining("test")).thenReturn(lN);
		when(mR.findByCountryContaining("test")).thenReturn(lN);
		Movie oMM = new Movie();
		oMM.setOmdbreference("1111111111111111");
		when(mR.findById(1)).thenReturn(oMM);
		when(mR.findById(0)).thenReturn(null);
		when(mR.existsById(1)).thenReturn(true);
		when(mR.existsById(0)).thenReturn(false);
		when(mR.findByOmdbreference("tt3498820")).thenReturn(lN);
		
		List lN1 = new ArrayList<Movie>();
		List lN2 = new ArrayList<Movie>();
		Movie m = new Movie();
		m.setRtreference("Banned");
		lN2.add(m);
		when(mR.findByName("t")).thenReturn(lN1);
		when(mR.findByNameContaining("t")).thenReturn(lN1);
		when(mR.findByLanguageContaining("t")).thenReturn(lN1);
		when(mR.findByActorsContaining("t")).thenReturn(lN1);
		when(mR.findByCountryContaining("t")).thenReturn(lN1);
		when(mR.existsByOmdbreference("tt0051047")).thenThrow(NullPointerException.class);
		when(mR.findByOmdbreference("tt0051047")).thenReturn(lN2);
		when(mR.findAll()).thenReturn(lN);
		
		cR = mock(CustomerRepository.class);
		Customer c = new Customer();
		when(cR.findById(1)).thenReturn(c);
		
		mCR = mock(MovieCommentRepository.class);
		MovieComment com = new MovieComment();
		com.setCustomerId(1);
		List lC = new ArrayList<MovieComment>();
		lC.add(com);
		when(mCR.getOne(1)).thenReturn(com);
		when(mCR.getOne(0)).thenReturn(null);
		when(mCR.existsMovieCommentByCustomerIdAndMovieId(1, 1)).thenReturn(true);
		when(mCR.findMovieCommentByMovieIdOrderByDate(1)).thenReturn(lC);
		
		cA = mock(CsvApi.class);
		String[] sA = {"1", "test", "test|ntest"};
		when(cA.search("1111111", "links")).thenReturn(sA);
		when(cA.search("1", "movies")).thenReturn(sA);
		List<String> nms = new ArrayList();
		nms.add("Captain America: Civil War");
		when(cA.recommendMovieIds("null|null", 3)).thenReturn(nms);
		when(cA.recommendMovieIds("test|ntest", 5)).thenReturn(nms);
		
		mRR = mock(MovieRecommendRepository.class);
		when(mRR.existsMovieRecommendByCustomerIdAndAndTag(16, "test")).thenReturn(true);
		when(mRR.findMovieRecommendByCustomerIdAndTag(16, "test")).thenReturn(new MovieRecommend());
		when(mRR.existsMovieRecommendByCustomerIdAndAndTag(17, "test")).thenReturn(false);
		
		List<MovieRecommend> mrs = new ArrayList();
		mrs.add(new MovieRecommend());
		mrs.add(new MovieRecommend());
		when(mRR.findTop2ByCustomerIdOrderByWeightsDesc(1)).thenReturn(mrs);
		
	}
	
	@Test
	public void testCreateMap(){
		MovieController mc = new MovieController();
		Movie m = new Movie();
		Movie m1 = new Movie();
		m1.setRtreference("Banned");
		List l1 = new ArrayList();
		l1.add(m);
		l1.add(m1);
		JSONArray map = new JSONArray();
		map.add(new JSONObject(m.toMap()));
		
		assertEquals(map, mc.createMap(l1));
		
	}
	
	@Test
	public void testSearchMovies() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/search/").param("name", "test")).andExpect(status().isOk());
		mock.perform(get("/api/movie/search/").param("name", "t")).andExpect(status().isOk());
		
	}
	
	@Test
	public void testAPIConnector() throws Exception{
		MovieController mc = new MovieController();
		assertEquals(new JSONObject(), mc.apiConnector(4, "test"));
		
	}
	
	@Test
	public void testGetMovie() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/get/").param("userId", "16").param("movieId", "168")).andExpect(status().isOk());
		mock.perform(get("/api/movie/get/").param("userId", "16").param("movieId", "1")).andExpect(status().isOk());
		mock.perform(get("/api/movie/get/").param("userId", "17").param("movieId", "1")).andExpect(status().isOk());
		mock.perform(get("/api/movie/get/").param("userId", "16").param("movieId","0")).andExpect(status().isOk());
		//assertEquals(ResponseEntity.ok().body(new JSONObject()),
		//		mock.perform(get("api/movie/search?name=test")).andExpect(status().isOk()));
	}
	
	@Test
	public void testAddComment() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
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
		
		JSONObject json2 = new JSONObject();
		json2.put("customerId", "0");
		json2.put("movieId", "0");
		json2.put("review", "arse");
		json2.put("score", "3");
		
		mock.perform(post("/api/movie/addComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json2.toJSONString())
				).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteComment() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
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
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("id", "1");
		json.put("review", "test");
		json.put("score", "3");
		
		mock.perform(post("/api/movie/updateComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
		
		JSONObject json1 = new JSONObject();
		json1.put("id", "0");
		json1.put("review", "test");
		json1.put("score", "3");
		
		mock.perform(post("/api/movie/updateComment/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json1.toJSONString())
				).andExpect(status().isOk());
	}
	
	@Test
	public void testDeleteMovie() throws Exception{
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("movieId", "1");
		json.put("loggedInUserId", "1");
		
		mock.perform(post("/api/deleteMovie/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
		
		JSONObject json1 = new JSONObject();
		json1.put("movieId", "0");
		json1.put("loggedInUserId", "0");
		
		mock.perform(post("/api/deleteMovie/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json1.toJSONString())
				).andExpect(status().isOk());
	}
	

	@Test
	public void testMainSearch() {
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
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
		
		JSONObject nF = new JSONObject();
		nF.put("message", "Not Found");
		
		assertEquals(false, mc.mainSearch("Captain America: Civil War").isEmpty());
		assertEquals(nF, mc.mainSearch("qiu3rb[q[54pu9nbpiebv9qp34bp9gqb3uvyboiqrubvoiqbgybviyrbi"));
	}
	
	@Test
	public void testMovieList() throws Exception {
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/init/").param("name", "new")).andExpect(status().isOk());
		mock.perform(get("/api/movie/init/").param("name", "top")).andExpect(status().isOk());
		mock.perform(get("/api/movie/init/").param("name", "all")).andExpect(status().isOk());
		mock.perform(get("/api/movie/init/").param("name", "h*ck")).andExpect(status().isOk());
	}
	
	@Test
	public void testRecommendMovie() throws Exception {
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		JSONObject json = new JSONObject();
		json.put("userId", "1");
		
		mock.perform(post("/api/movie/recommend/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.toJSONString())
				).andExpect(status().isOk());
	}

	@Test
	public void getSimilarMovie() throws Exception {
		MovieController mc = new MovieController(mR, cR, mCR, cA, mRR);
		MockMvc mock = MockMvcBuilders.standaloneSetup(mc).build();
		mock.perform(get("/api/movie/similar/").param("id", "1")).andExpect(status().isOk());
		mock.perform(get("/api/movie/similar/").param("id", "0")).andExpect(status().isOk());
	}
}



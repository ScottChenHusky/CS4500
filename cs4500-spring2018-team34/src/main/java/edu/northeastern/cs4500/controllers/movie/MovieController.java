package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.


import edu.northeastern.cs4500.repositories.CustomerRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieController {
  private String log;
  String[] filterList = {
          "arse", "asshole","bitch","cunt","fuck","nigga","nigger"," ass ","ass hole"
  };
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private MovieCommentRepository movieCommentRepository;

  public Map<String, JSONObject> createMap(String type, List input, String who) {
    Map<String, JSONObject> map = new HashMap();
    for (int i = 0; i < input.size(); i++) {
      if(who.equals("Movie")){
        Movie m = (Movie) input.get(i);
        map.put(type + i, new JSONObject(m.toMap()));
      } else {
        MovieComment m = (MovieComment) input.get(i);
        map.put(type + i, new JSONObject(m.toMap()));
      }

    }

    return map;
  }

  @RequestMapping(path = "/api/movie/search", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> searchMovies(@RequestParam(name = "name") String searchby) {
    List<Movie> movie = movieRepository.findByName(searchby);
    List<Movie> moviesName = movieRepository.findByNameContaining(searchby);
    List<Movie> moviesLanguage = movieRepository.findByLanguageContaining(searchby);
    List<Movie> moviesActors = movieRepository.findByActorsContaining(searchby);
    List<Movie> moviesCountry = movieRepository.findByCountryContaining(searchby);
    Map<String, JSONObject> map = new HashMap();
    JSONObject json = new JSONObject();
    if (movie.size() == 0) {
      StringBuffer response = new StringBuffer();
      try{
        String requestUrl = "http://www.omdbapi.com/?s="+searchby+ "&apikey=a65196c5";
        URL url = new URL(requestUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        InputStream stream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String line;
        while((line = buffer.readLine()) != null){
          response.append(line);
        }
        buffer.close();
        connection.disconnect();
        String result = response.toString();
        JSONParser jsonParser = new JSONParser();
        JSONObject movieJSON = (JSONObject) jsonParser.parse(result);
        json.put("movie", movieJSON);

      } catch (IOException | ParseException e) {
        log = e.toString();
      }
      json.put("message", "not found");
    } else {
      json.put("message", "found");
    }


    map.putAll(createMap("Movie", movie, "Movie"));
    map.putAll(createMap("Name", moviesName, "Movie"));
    map.putAll(createMap("Actor", moviesActors, "Movie"));
    map.putAll(createMap("Language", moviesLanguage, "Movie"));
    map.putAll(createMap("Country", moviesCountry, "Movie"));
    json.putAll(map);
    return ResponseEntity.ok().body(json);
  }


//  public void parseFromOMDB(String input){
//
//
//    Movie movie = new Movie()
//            .withLanguage(source.get("Language").toString())
//            .withDate((source.get("Released").toString()))
//            .withDescription(source.get("Plot").toString())
//            .withLevel(source.get("Rated").toString())
//            .withName(source.get("Title").toString())
//            .withScore(source.get("imdbRating").toString())
//            .withTime(source.get("Runtime").toString())
//            .withOmdbreference(source.get("imdbID").toString())
//            .withRtreference("")
//            .withTmdbreference("")
//            .withDirector(source.get("Director").toString())
//            .withActors(source.get("Actors").toString())
//            .withCountry(source.get("Country").toString())
//            .withAwards(source.get("Awards").toString())
//            .withBoxOffice(source.get("BoxOffice").toString())
//            .withPoster(source.get("Poster").toString());
//
//  }

  @RequestMapping(path = "/api/movie/get", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getMovie(@RequestParam(name = "id") String searchId) {
    Movie movie = movieRepository.findById(Integer.parseInt(searchId));
    JSONObject json = new JSONObject();
    if (movie == null) {
      json.put("message", "not found");
    } else {
      Integer search = Integer.parseInt(searchId);
      List<MovieComment> comments = movieCommentRepository.findMovieCommentByMovieIdOrderByDate(search);
      json.put("message", "found");
      json.put("movie", new JSONObject(movie.toMap()));
      JSONArray array = new JSONArray();
      for(MovieComment mc: comments){
        JSONObject temp = new JSONObject(mc.toMap());
        temp.put("username", customerRepository.findById(mc.getCustomer_id()).getUsername());
        array.add(temp);
      }
      json.put("comment", array);

    }
      return ResponseEntity.ok().body(json);

  }

  @RequestMapping(path = "/api/movie/addComment", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> addComment(@RequestBody JSONObject source){
    Integer customerId = Integer.parseInt(source.get("customerId").toString());
    Integer movieId = Integer.parseInt(source.get("movieId").toString());
    JSONObject json = new JSONObject();
    if (movieCommentRepository.existsMovieCommentByCustomerIdAndMovieId(customerId, movieId)){
      json.put("message","exist");
      return ResponseEntity.ok().body(json);
    } else {
      String inputData = source.get("review").toString();
      boolean bad = false;
      for(String s: filterList){
        if(inputData.toLowerCase().contains(s)){
          bad = true;
          break;
        }
      }

        if(!bad){
          MovieComment movieComment = new MovieComment(
                  inputData,
                  source.get("score").toString(),
                  new Date(),
                  customerId,
                  movieId
          );
          movieCommentRepository.save(movieComment);
          json.put("message", "success");
        } else {
          json.put("message", "bad words");
        }

      }


      return ResponseEntity.ok().body(json);
    }



  @RequestMapping(path = "/api/movie/deleteComment", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> deleteComment(@RequestBody JSONObject source){
    Integer customerId = Integer.parseInt(source.get("customerId").toString());
    Integer movieId = Integer.parseInt(source.get("movieId").toString());
    JSONObject json = new JSONObject();
    if (movieCommentRepository.existsMovieCommentByCustomerIdAndMovieId(customerId, movieId)){
      movieCommentRepository.deleteMovieCommentByCustomerIdAndMovieId(customerId, movieId);
      json.put("message","success");
      return ResponseEntity.ok().body(json);
    } else {
      json.put("message", "does not exist");
      return ResponseEntity.ok().body(json);
    }

  }

  @RequestMapping(path = "/api/movie/updateComment",method = RequestMethod.POST)
  public ResponseEntity<JSONObject> updateComment(@RequestBody JSONObject source){
    JSONObject json = new JSONObject();
    try{
      Integer movieCommentId = Integer.parseInt(source.get("id").toString());
      MovieComment movieComment = movieCommentRepository.getOne(movieCommentId);
      movieComment.setDate(new Date());
      movieComment.setReview(source.get("review").toString());
      movieComment.setScore(source.get("score").toString());
      movieCommentRepository.save(movieComment);
      json.put("message","success");

    } catch(Exception e) {
      json.put("message", "failed");
      json.put("error", e.toString());

    }
    return ResponseEntity.ok().body(json);

  }
}

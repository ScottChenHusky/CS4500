package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieController {
  @Autowired
  private MovieRepository movieRepository;

  @RequestMapping(path = "/api/movie/search", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> searchMovies(@RequestBody JSONObject request) {
    List<Movie> movie = movieRepository.findByName(request.get("name").toString());
    if (movie == null) {
      Map<String, String> map = new HashMap();
      map.put("message","movie not found");
      JSONObject json = new JSONObject(map);
      return ResponseEntity.badRequest().body(json);
    } else {
      Map<String, String> map = new HashMap();
      map.put("message","movie found");
      map.put("length",Integer.toString(movie.size()));
      for(int i = 0; i < movie.size(); i++){
        Map<String, String> temp = movie.get(i).toMap();
        JSONObject tempJson = new JSONObject(temp);
        map.put(Integer.toString(i), tempJson.toString());
      }
      JSONObject json = new JSONObject(map);
      return ResponseEntity.ok().body(json);
    }
  }

    @RequestMapping(path = "/api/movie/addMovieFromOMDB", method = RequestMethod.POST)
  public void addMovies(@RequestBody JSONObject source){
      Movie movie = new Movie()
              .withLanguage(source.get("Language").toString())
              .withDate(source.get("Released").toString())
              .withDescription(source.get("Plot").toString())
              .withLevel(source.get("Rated").toString())
              .withName(source.get("Title").toString())
              .withScore(Float.valueOf(source.get("imdbRating").toString()))
              .withTime(source.get("Runtime").toString())
              .withOmdbreference(source.get("imdbID").toString())
              .withRtreference("")
              .withTmdbreference("")
              .withDirector(source.get("Director").toString())
              .withActors(source.get("Actors").toString())
              .withCountry(source.get("Country").toString())
              .withAwards(source.get("Awards").toString())
              .withBoxOffice(source.get("BoxOffice").toString())
              .withPoster(source.get("Poster").toString());

      movieRepository.save(movie);

  }
  @RequestMapping(path = "/api/movie/get", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> getCustomer(@RequestBody JSONObject request) {
    Movie movie = movieRepository.findById(Integer.parseInt(request.get("Id").toString()));
    if (movie == null) {
      Map<String, String> map = new HashMap();
      map.put("message","movie not found");
      JSONObject json = new JSONObject(map);
      return ResponseEntity.badRequest().body(json);
    } else {
      Map<String, String> map = new HashMap();
      map.put("message","movie found");
      map.put("movie", movie.toMap().toString());
      JSONObject json = new JSONObject(map);
      return ResponseEntity.ok().body(json);
    }
  }
}

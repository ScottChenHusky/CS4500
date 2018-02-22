package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieController {
  @Autowired
  private MovieRepository movieRepository;

  public Map<String, String> createMap(String type, List<Movie> input) {
    Map<String, String> map = new HashMap();
    for (int i = 0; i < input.size(); i++) {
      Map<String, String> temp = input.get(i).toMap();
      JSONObject tempJson = new JSONObject(temp);
      map.put(type + Integer.toString(i), tempJson.toString());

    }
    return map;
  }

  @RequestMapping(path = "/api/movie/search", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> searchMovies(@RequestParam(name = "name") String searchby) {
    List<Movie> movie = movieRepository.findByName(searchby);
    List<Movie> moviesName = movieRepository.findByNameLike(searchby);
    List<Movie> moviesLanguage = movieRepository.findByLanguageLike(searchby);
    List<Movie> moviesActors = movieRepository.findByActorsLike(searchby);
    List<Movie> moviesCountry = movieRepository.findByCountryLike(searchby);
    Map<String, String> map = new HashMap();
    if (movie == null) {

//      try {
//        String url = "http://www.omdbapi.com/";
//        String charset = "UTF-8";
//        String query = String.format("t=%s&season=%s&episode=%s",
//                URLEncoder.encode(searchby, charset));
//        URLConnection connection = new URL(url + "?" + query).openConnection();
//        connection.setRequestProperty("Accept-Charset", charset);
//        InputStream response = connection.getInputStream();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
      map.put("message", "movie not found");
    } else {
      map.put("message", "movie found");
    }


    map.putAll(createMap("Movie", movie));
    map.putAll(createMap("Name", moviesName));
    map.putAll(createMap("Actor", moviesActors));
    map.putAll(createMap("Language", moviesLanguage));
    map.putAll(createMap("Country", moviesCountry));
    JSONObject json = new JSONObject(map);
    return ResponseEntity.ok().body(json);
  }


  @RequestMapping(path = "/api/movie/addMovieFromOMDB", method = RequestMethod.POST)
  public void addMovies(@RequestBody JSONObject source) {
    Movie movie = new Movie()
            .withLanguage(source.get("Language").toString())
            .withDate(source.get("Released").toString())
            .withDescription(source.get("Plot").toString())
            .withLevel(source.get("Rated").toString())
            .withName(source.get("Title").toString())
            .withScore(source.get("imdbRating").toString())
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
  public ResponseEntity<JSONObject> getMovie(@RequestBody JSONObject request) {
    Movie movie = movieRepository.findById(Integer.parseInt(request.get("Id").toString()));
    if (movie == null) {
      Map<String, String> map = new HashMap();
      map.put("message", "movie not found");
      JSONObject json = new JSONObject(map);
      return ResponseEntity.badRequest().body(json);
    } else {
      Map<String, String> map = new HashMap();
      map.put("message", "movie found");
      map.put("movie", movie.toMap().toString());
      JSONObject json = new JSONObject(map);
      return ResponseEntity.ok().body(json);
    }
  }
}
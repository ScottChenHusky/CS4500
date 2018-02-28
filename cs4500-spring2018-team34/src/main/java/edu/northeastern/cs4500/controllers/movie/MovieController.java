package java.edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

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

  public Map<String, JSONObject> createMap(String type, List<Movie> input) {
    Map<String, JSONObject> map = new HashMap();
    for (int i = 0; i < input.size(); i++) {
      Movie m = input.get(i);
      map.put(type + i, new JSONObject(m.toMap()));
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
      json.put("message", "not found");
    } else {
      json.put("message", "found");
    }


    map.putAll(createMap("Movie", movie));
    map.putAll(createMap("Name", moviesName));
    map.putAll(createMap("Actor", moviesActors));
    map.putAll(createMap("Language", moviesLanguage));
    map.putAll(createMap("Country", moviesCountry));
    json.putAll(map);
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

  @RequestMapping(path = "/api/movie/get", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getMovie(@RequestParam(name = "id") String searchId) {
    Movie movie = movieRepository.findById(Integer.parseInt(searchId));
    Map<String, JSONObject> map = new HashMap();
    JSONObject json = new JSONObject();
    if (movie == null) {
      json.put("message", "not found");

    } else {
      json.put("message", "found");
      map.put("movie", new JSONObject(movie.toMap()));

    }
      json.putAll(map);
      return ResponseEntity.ok().body(json);

  }

}

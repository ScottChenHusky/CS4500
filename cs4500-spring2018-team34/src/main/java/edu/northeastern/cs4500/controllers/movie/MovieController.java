package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.


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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import edu.northeastern.cs4500.repositories.CustomerRepository;

@RestController
public class MovieController {
  final static Logger log = Logger.getLogger("MovieController");
  String[] filterList = {
          "arse", "asshole", "bitch", "cunt", "fuck", "nigga", "nigger", " ass ", "ass hole"
  };
  
  
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private MovieCommentRepository movieCommentRepository;
  
  public MovieController(MovieRepository m, CustomerRepository c, MovieCommentRepository mcr) {
	  this.movieRepository = m;
	  this.customerRepository = c;
	  this.movieCommentRepository = mcr;
  }
  
  public MovieController() {
	  
  }

  protected JSONArray createMap(List input) {
    JSONArray result = new JSONArray();
    for (Object o : input) {
      Movie m = (Movie)o;
      if(m.getRtreference() == null || !m.getRtreference().equals("Banned")){
        result.add(new JSONObject(m.toMap()));
      }

    }
    return result;
  }

  @RequestMapping(path = "/api/movie/search", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> searchMovies(@RequestParam(name = "name") String searchby) {
    JSONObject logInfo = new JSONObject();
    List<Movie> movie = movieRepository.findByName(searchby);
    List<Movie> moviesName = movieRepository.findByNameContaining(searchby);
    List<Movie> moviesLanguage = movieRepository.findByLanguageContaining(searchby);
    List<Movie> moviesActors = movieRepository.findByActorsContaining(searchby);
    List<Movie> moviesCountry = movieRepository.findByCountryContaining(searchby);
    JSONObject json = new JSONObject();
    logInfo.put("Task", "MovieSearch");
    logInfo.put("SearchBy", searchby);
    JSONObject movieResult = new JSONObject();
    if (movie.isEmpty()) {
      movieResult = mainSearch(searchby);
    } else {
      movieResult = new JSONObject(movie.get(0).toMap());
    }
    json.put("Movie", movieResult);
    json.put("Name", createMap(moviesName));
    json.put("Actor", createMap(moviesActors));
    json.put("Language", createMap(moviesLanguage));
    json.put("Country", createMap(moviesCountry));
    log.finest(logInfo.toString());
    return ResponseEntity.ok().body(json);
  }

  protected JSONObject apiConnector(int type, String searchBy) {
    JSONObject logInfo = new JSONObject();
    String requestUrl = "";
    switch (type) {
      case 0:
        // get all possible results from tmdb
        searchBy = searchBy.replaceAll(" ", "%20");
        requestUrl = "https://api.themoviedb.org/3/search/movie?api_key=b9868fe82432f75509f2f546b5a2b791&query=" + searchBy;
        break;
      case 1:
        // get the tmdb movie detail
        requestUrl = "https://api.themoviedb.org/3" + "/movie/" + searchBy + "?api_key=b9868fe82432f75509f2f546b5a2b791";
        break;
      case 2:
        // get the omdb movie detail
        requestUrl = "http://www.omdbapi.com/?i=" + searchBy + "&apikey=a65196c5";
        break;
      case 3:
        // get the youtube trailer detail
        requestUrl = "https://api.themoviedb.org/3/movie/" + searchBy + "/videos?api_key=b9868fe82432f75509f2f546b5a2b791";
        break;
      default:
        requestUrl = "Invalid month";
        break;
    }
    JSONObject jsonObject = new JSONObject();
    URL url = null;
    try {
      StringBuffer response = new StringBuffer();
      url = new URL(requestUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "*/*");
      connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      InputStream stream = connection.getInputStream();
      InputStreamReader reader = new InputStreamReader(stream);
      BufferedReader buffer = new BufferedReader(reader);
      String line;
      while ((line = buffer.readLine()) != null) {
        response.append(line);
      }
      buffer.close();
      connection.disconnect();
      String result = response.toString();
      JSONParser jsonParser = new JSONParser();
      jsonObject = (JSONObject) jsonParser.parse(result);
    } catch (ParseException | IOException e) {
      logInfo.put("Exception", e.toString());
    }
    logInfo.put("Task", "ApiConnector");
    logInfo.put("Type", type);
    logInfo.put("SearchBy", searchBy);
    log.finest(logInfo.toString());
    return jsonObject;
  }

  public JSONObject mainSearch(String searchby) {
    JSONObject movieJSON = apiConnector(0, searchby);
    JSONObject finalR = new JSONObject();
    if(Integer.parseInt(movieJSON.get("total_results").toString()) == 0){
      finalR.put("message", "Not Found");
    } else {
      Movie m = tmdbParser(movieJSON);
      finalR.put("Results" , new JSONObject(m.toMap()));
      finalR.put("message", "Found");
    }
    return finalR;
  }


  @RequestMapping(path = "/api/movie/get", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getMovie(@RequestParam(name = "id") String searchId) {
    JSONObject logInfo = new JSONObject();
    Movie movie = movieRepository.findById(Integer.parseInt(searchId));
    JSONObject json = new JSONObject();
    if (movie == null) {
      logInfo.put("message", "not found");
      json.put("message", "not found");
    } else {
      Integer search = Integer.parseInt(searchId);
      List<MovieComment> comments = movieCommentRepository.findMovieCommentByMovieIdOrderByDate(search);
      json.put("message", "found");
      logInfo.put("message", "found");
      json.put("movie", new JSONObject(movie.toMap()));
      JSONArray array = new JSONArray();
      for (MovieComment mc : comments) {
        JSONObject temp = new JSONObject(mc.toMap());
        temp.put("username", customerRepository.findById(mc.getCustomerId()).getUsername());
        array.add(temp);
      }
      json.put("comment", array);

    }
    logInfo.put("Task", "getMovie");
    logInfo.put("id", searchId);
    log.finest(logInfo.toString());
    return ResponseEntity.ok().body(json);

  }

  @RequestMapping(path = "/api/movie/addComment", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> addComment(@RequestBody JSONObject source) {
    JSONObject logInfo = new JSONObject();
    Integer customerId = Integer.parseInt(source.get("customerId").toString());
    Integer movieId = Integer.parseInt(source.get("movieId").toString());
    JSONObject json = new JSONObject();
    if (movieCommentRepository.existsMovieCommentByCustomerIdAndMovieId(customerId, movieId)) {
      json.put("message", "exist");
      logInfo.put("message", "exist");
      return ResponseEntity.ok().body(json);
    } else {
      String inputData = source.get("review").toString();
      boolean bad = false;
      for (String s : filterList) {
        if (inputData.toLowerCase().contains(s)) {
          bad = true;
          break;
        }
      }

      if (!bad) {
        MovieComment movieComment = new MovieComment(
                inputData,
                source.get("score").toString(),
                new Date(),
                customerId,
                movieId
        );
        movieCommentRepository.save(movieComment);
        json.put("message", "success");
        logInfo.put("message", "success");
      } else {
        json.put("message", "bad words");
        logInfo.put("message", "bad words");
      }

    }
    logInfo.put("Task", "addComment");
    logInfo.put("CustomerId", customerId);
    logInfo.put("MovieId", movieId);

    log.finest(logInfo.toString());
    return ResponseEntity.ok().body(json);
  }


  @RequestMapping(path = "/api/movie/deleteComment", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> deleteComment(@RequestBody JSONObject source) {
    JSONObject logInfo = new JSONObject();
    Integer customerId = Integer.parseInt(source.get("customerId").toString());
    Integer movieId = Integer.parseInt(source.get("movieId").toString());
    JSONObject json = new JSONObject();
    if (movieCommentRepository.existsMovieCommentByCustomerIdAndMovieId(customerId, movieId)) {
      movieCommentRepository.deleteMovieCommentByCustomerIdAndMovieId(customerId, movieId);
      json.put("message", "success");
      logInfo.put("message", "success");
    } else {
      json.put("message", "does not exist");
      logInfo.put("message", "does not exist");
    }
    logInfo.put("Task", "deleteComment");
    logInfo.put("CustomerId", customerId);
    logInfo.put("MovieId", movieId);
    log.finest(logInfo.toString());
    return ResponseEntity.ok().body(json);
  }

  @RequestMapping(path = "/api/movie/updateComment", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> updateComment(@RequestBody JSONObject source) {
    JSONObject logInfo = new JSONObject();
    logInfo.put("Task", "updateComment");
    JSONObject json = new JSONObject();
    try {
      Integer movieCommentId = Integer.parseInt(source.get("id").toString());
      logInfo.put("MovieCommentId", movieCommentId);
      MovieComment movieComment = movieCommentRepository.getOne(movieCommentId);
      movieComment.setDate(new Date());
      movieComment.setReview(source.get("review").toString());
      movieComment.setScore(source.get("score").toString());
      movieCommentRepository.save(movieComment);
      json.put("message", "success");
      logInfo.put("message", "success");
      log.finest(logInfo.toString());
    } catch (Exception e) {
      json.put("message", "failed");
      logInfo.put("Exception", e.toString());
      log.warning(logInfo.toString());
    }
    return ResponseEntity.ok().body(json);

  }

  @RequestMapping(path = "/api/deleteMovie", method = RequestMethod.POST)
  public ResponseEntity<JSONObject> deleteMovie(@RequestBody JSONObject source) {
    JSONObject logInfo = new JSONObject();
    logInfo.put("Task", "deleteMovie");
    Integer movieId = Integer.parseInt(source.get("movieId").toString());
    Integer customerId = Integer.parseInt(source.get("loggedInUserId").toString());
    logInfo.put("MovieId", movieId);
    logInfo.put("CustomerId", customerId);
    JSONObject jsonObject = new JSONObject();
    Movie movie = movieRepository.findById(movieId);
    if (movieRepository.existsById(movieId)) {
      movie.setRtreference("Banned");
      movieRepository.save(movie);
      jsonObject.put("message", "ok");
      logInfo.put("message", "ok");
    } else {
        jsonObject.put("message", "failed");
        logInfo.put("message", "failed");
    }
    log.finest(logInfo.toString());
    return ResponseEntity.ok().body(jsonObject);
  }

  public Movie tmdbParser(JSONObject source) {
    JSONObject logInfo = new JSONObject();
    logInfo.put("Task", "tmdbParser");
    Movie movie = new Movie();
    // convert the json from tmdb to list of tmdbid
    JSONArray array = (JSONArray) source.get("results");

    JSONObject temp = (JSONObject) array.get(0);
    String id = temp.get("id").toString();
    logInfo.put("id", id);
    JSONObject tmdb = apiConnector(1, id);

    String imdbId = tmdb.get("imdb_id").toString();
    logInfo.put("imdbId", imdbId);
    JSONObject omdb = apiConnector(2, imdbId);

    JSONObject trailer = apiConnector(3, id);
    JSONArray jsonArray = (JSONArray) trailer.get("results");

    List<String> trailers = new ArrayList<>();
    for (Object o : jsonArray) {
      JSONObject t = (JSONObject) o;
      trailers.add(t.get("key").toString());
    }

    for (int i = 0; i < 3; i++) {
      trailers.add("");
    }
    try {
      movie.withName(tmdb.get("title").toString())
              .withDate(tmdb.get("release_date").toString())
              .withScore(tmdb.get("vote_average").toString())
              .withDescription(tmdb.get("overview").toString())
              .withLanguage(tmdb.get("original_language").toString())
              .withTime(tmdb.get("runtime").toString())
              .withOmdbreference(imdbId)
              .withLevel(omdb.get("Rated").toString())
              .withTmdbreference("")
              .withRtreference("")
              .withDirector(omdb.get("Director").toString())
              .withActors(omdb.get("Actors").toString())
              .withCountry(omdb.get("Country").toString())
              .withAwards(omdb.get("Awards").toString())
              .withBoxOffice(omdb.get("BoxOffice").toString())
              .withPoster(omdb.get("Poster").toString())
              .withT1(trailers.get(0))
              .withT2(trailers.get(1))
              .withT3(trailers.get(2));
      System.out.println(imdbId);
      if (!movieRepository.existsByOmdbreference(imdbId)) {
        movieRepository.save(movie);

      }

    } catch (NullPointerException e) {
      logInfo.put("Exception", e.toString());
      log.warning(logInfo.toString());
    }
    log.finest(logInfo.toString());
    System.out.println(imdbId);
    movie = movieRepository.findByOmdbreference(imdbId).get(0);
    if(movie.getRtreference() == null || !movie.getRtreference().equals("Banned")){
      return movie;
    } else {
      return null;
    }


  }

}

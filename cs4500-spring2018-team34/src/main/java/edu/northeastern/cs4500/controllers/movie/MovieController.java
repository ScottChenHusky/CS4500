package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 2/15/18.

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
  @Autowired
  private MovieRepository movieRepository;

  @RequestMapping(path = "/api/movie/search", method = RequestMethod.POST)
  public ResponseEntity<SearchMovieResponseJSON> searchMovies(@RequestBody SearchMovieRequestJSON request) {
    List<Movie> movie = movieRepository.findByName(request.getName());
    if (movie == null) {
      return ResponseEntity.badRequest().body(
              new SearchMovieResponseJSON()
                      .withMessage("movie not found")
      );
    } else {
      return ResponseEntity.ok().body(
              new SearchMovieResponseJSON()
                      .withMovie(movie)
                      .withMessage("movie found")
      );
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
              .withTmdbreference("");
      movieRepository.save(movie);

  }
}

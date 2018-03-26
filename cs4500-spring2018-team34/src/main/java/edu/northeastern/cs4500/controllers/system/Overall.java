package edu.northeastern.cs4500.controllers.system;// Created by xuanyuli on 3/25/18.

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import edu.northeastern.cs4500.controllers.movie.MovieCommentRepository;
import edu.northeastern.cs4500.controllers.movie.MovieRepository;

@RestController
public class Overall {

  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private MovieCommentRepository movieCommentRepository;

  @RequestMapping(path = "/api/system", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getSystemInfo() {
    JSONObject json = new JSONObject();
    /* Total number of processors or cores available to the JVM */
    json.put("Available processor (cores)", Runtime.getRuntime().availableProcessors());

    /* Total amount of free memory available to the JVM */
    json.put("Free memory (bytes)", Runtime.getRuntime().freeMemory());

    /* This will return Long.MAX_VALUE if there is no preset limit */
    long maxMemory = Runtime.getRuntime().maxMemory();
    /* Maximum amount of memory the JVM will attempt to use */
    json.put("Maximum Memory (bytes)",(maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

    /* Total memory currently available to the JVM */
    json.put("Total Memory Available to JVM (bytes)", Runtime.getRuntime().totalMemory());

    /* Get a list of all filesystem roots on this system */
    File[] roots = File.listRoots();
    JSONArray jsonArray = new JSONArray();
    /* For each filesystem root, print some info */
    for (File root : roots) {
      JSONObject temp = new JSONObject();
      temp.put("File System Root", root.getAbsolutePath());
      temp.put("Total Space (bytes)", root.getTotalSpace());
      temp.put("Free Space (bytes)",root.getFreeSpace());
      temp.put("Usable Space (bytes)", root.getUsableSpace());
      jsonArray.add(temp);
    }
    json.put("Root",jsonArray);
    json.put("Movie Count", movieRepository.count());
    json.put("Movie Comment Count", movieCommentRepository.count());
    return ResponseEntity.ok().body(json);
  }
}

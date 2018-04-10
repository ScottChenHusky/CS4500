package edu.northeastern.cs4500.controllers.system;// Created by xuanyuli on 3/25/18.

import edu.northeastern.cs4500.controllers.movie.MovieCommentRepository;
import edu.northeastern.cs4500.controllers.movie.MovieRepository;
import edu.northeastern.cs4500.repositories.CustomerFollowingRepository;
import edu.northeastern.cs4500.repositories.CustomerRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.logging.Logger;

@RestController
public class Overall {

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private CustomerFollowingRepository customerFollowingRepository;
  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private MovieCommentRepository movieCommentRepository;

  final static Logger log = Logger.getLogger("SystemMonitor");

  @RequestMapping(path = "/api/system", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getSystemInfo() {
    JSONObject logInfo = new JSONObject();
    JSONObject json = new JSONObject();
    logInfo.put("Task", "getSystemInfo");
    json.put("Available processor (cores)", Runtime.getRuntime().availableProcessors());
    json.put("Free memory (bytes)", Runtime.getRuntime().freeMemory());
    long maxMemory = Runtime.getRuntime().maxMemory();
    json.put("Maximum Memory (bytes)", (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
    json.put("Total Memory Available to JVM (bytes)", Runtime.getRuntime().totalMemory());
    File[] roots = File.listRoots();
    JSONArray jsonArray = new JSONArray();
    for (File root : roots) {
      JSONObject temp = new JSONObject();
      temp.put("File System Root", root.getAbsolutePath());
      temp.put("Total Space (bytes)", root.getTotalSpace());
      temp.put("Free Space (bytes)", root.getFreeSpace());
      temp.put("Usable Space (bytes)", root.getUsableSpace());
      jsonArray.add(temp);
    }
    json.put("Root", jsonArray);
    json.put("User Count", customerRepository.count());
    json.put("User Following Count", customerFollowingRepository.count());
    json.put("Movie Count", movieRepository.count());
    json.put("Movie Comment Count", movieCommentRepository.count());
    logInfo.put("Result", json.toString());
    File file = new File("myapplication.log");
    double bytes = 0;
    if (file.exists()) {
      bytes = file.length();
    }
    json.put("Current Log File Size", bytes);
    log.fine(logInfo.toString());
    return ResponseEntity.ok().body(json);
  }
}

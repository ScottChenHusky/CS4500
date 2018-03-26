package edu.northeastern.cs4500.controllers.system;// Created by xuanyuli on 3/25/18.

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.logging.Logger;

import edu.northeastern.cs4500.controllers.movie.MovieCommentRepository;
import edu.northeastern.cs4500.controllers.movie.MovieRepository;

@RestController
public class Overall {

  @Autowired
  private MovieRepository movieRepository;
  @Autowired
  private MovieCommentRepository movieCommentRepository;

  final static Logger log = Logger.getLogger("SystemMonitor");

  @RequestMapping(path = "/api/system", method = RequestMethod.GET)
  public ResponseEntity<JSONObject> getSystemInfo() {
    JSONObject logInfo = new JSONObject();
    JSONObject json = new JSONObject();
    logInfo.put("Task", logInfo);
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
    json.put("Movie Count", movieRepository.count());
    json.put("Movie Comment Count", movieCommentRepository.count());
    logInfo.put("Result", json);
    log.fine(logInfo.toJSONString());
    return ResponseEntity.ok().body(json);
  }
}

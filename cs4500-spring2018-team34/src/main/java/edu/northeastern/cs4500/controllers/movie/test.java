package edu.northeastern.cs4500.controllers.movie;// Created by xuanyuli on 3/25/18.

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class test {
  public static void main(String[] args) throws IOException, ParseException {
    StringBuffer omdbResponse = new StringBuffer();
    String imdbId ="tt0848228";
    URL url = null;
    String requestUrl = "http://www.omdbapi.com/?i=" + imdbId + "&apikey=a65196c5";
    url = new URL(requestUrl);
    HttpURLConnection omdbConnection = (HttpURLConnection)url.openConnection();
    omdbConnection.setRequestMethod("GET");
    omdbConnection.setRequestProperty("Accept", "*/*");
    omdbConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
    InputStream omdbStream = omdbConnection.getInputStream();
    InputStreamReader omdbReader = new InputStreamReader(omdbStream);
    BufferedReader omdbBuffer = new BufferedReader(omdbReader);
    String omdbLine;
    while((omdbLine = omdbBuffer.readLine()) != null){
      omdbResponse.append(omdbLine);
    }
    omdbBuffer.close();
    omdbConnection.disconnect();

    String omdbResult = omdbResponse.toString();
    JSONParser omdbJsonParser = new JSONParser();
    JSONObject omdbMovieJSON = (JSONObject) omdbJsonParser.parse(omdbResult);
    System.out.println(omdbMovieJSON.toJSONString());
  }
}

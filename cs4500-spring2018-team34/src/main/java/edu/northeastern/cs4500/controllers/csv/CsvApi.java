package edu.northeastern.cs4500.controllers.csv;// Created by xuanyuli on 4/8/18.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CsvApi {
  public List<String> search(String searchBy, String fileName) throws FileNotFoundException {
    String csvFile = fileName+".csv";
    Scanner scanner = new Scanner(new File(csvFile));
    int index = 0;
    if(fileName.equals("links")){
      index = 1;
    }
    while(scanner.hasNext()){
      List<String> line = new ArrayList<>(Arrays.asList(scanner.nextLine().split(",")));
      if(line.get(index).equals(searchBy)){
        scanner.close();
        return line;
      }
    }
    scanner.close();
    return null;
  }
  public String[] parseMovieTag(List<String> input){
    if(input == null){
      return null;
    } else {
      return input.get(2).split("\\|");
    }

  }

  public String getMovieId(List<String> input){
    if(input == null){
      return null;
    } else {
      return input.get(0);
    }

  }

  public List<String> recommendMovieIds(String input, int want) throws FileNotFoundException {
    String csvFile = "movies.csv";
    Scanner scanner = new Scanner(new File(csvFile));
    int get = 0;
    List<String> result = new ArrayList<>();
    while(scanner.hasNext() && get < want){
      List<String> line = new ArrayList<>(Arrays.asList(scanner.nextLine().split(",")));
      if(line.get(2).equals(input)){
        get++;
        String s = line.get(1);
        result.add(s.substring(0, s.length() - 7));
      }
    }
    scanner.close();
    return result;
  }
}

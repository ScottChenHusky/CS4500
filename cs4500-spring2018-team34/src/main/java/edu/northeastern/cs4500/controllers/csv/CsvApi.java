package edu.northeastern.cs4500.controllers.csv;// Created by xuanyuli on 4/8/18.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class CsvApi {
  private final Map<Integer, String> map = new HashMap<>();
  private Map<String, List<String>> tagsMap = new HashMap<>();
  public CsvApi(){
    try {
      Scanner scanner = new Scanner(new File("links.csv"));
      scanner.nextLine();
      while(scanner.hasNext()){
        String[] ss = scanner.nextLine().split(",");
        map.put(Integer.parseInt(ss[1]), ss[0]);
      }
      scanner.close();

      Scanner scanner1 = new Scanner(new File("movies.csv"));
      while(scanner1.hasNext()){
        String[] ss = scanner1.nextLine().split(",");
        int last = ss.length - 1;
        String name = ss[last];
        String[] s = name.split("\\|");
        if(s.length >= 2){
          name = s[0] + "|" + s[1];
        }
        List<String> temp = new ArrayList<>();
        if(tagsMap.containsKey(name)){
          temp = tagsMap.get(name);
        }
        String movie = ss[1];
        if(movie.contains("(") && movie.length() > 7){
          movie = movie.substring(0, movie.length()-7);
        }
        temp.add(movie);
        tagsMap.put(name, temp);

      }

      scanner1.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  public String[] search(String searchBy, String fileName) throws FileNotFoundException {

    if(fileName.equals("links")){
      try{
        String[] result = new String[1];
        result[0] = this.map.get(Integer.parseInt(searchBy));
        return result;
      } catch(NullPointerException e){
        return null;
      }

    } else {
      String csvFile = fileName+".csv";
      Scanner scanner = new Scanner(new File(csvFile));
      while(scanner.hasNext()){
        String[] line = scanner.nextLine().split(",");
        if(line[0].equals(searchBy)){
          scanner.close();
          return line;
        }
      }
      scanner.close();
    }

    return null;
  }


  public List<String> recommendMovieIds(String name, int want) {
    String[] ss = name.split("\\|");
    if(ss.length >= 2){
      name = ss[0] + "|" + ss[1];
    }
    List<String> result = new ArrayList<>(tagsMap.get(name));

    Set<String> finalL = new HashSet<>();
    for(int i = 0; i < want; i++){
      Random r = new Random();
      finalL.add(result.get(r.nextInt(result.size() + 1)));
    }
    return new ArrayList<>(finalL);
  }
}

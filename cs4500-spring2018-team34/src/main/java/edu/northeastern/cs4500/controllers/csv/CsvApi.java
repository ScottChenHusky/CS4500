package edu.northeastern.cs4500.controllers.csv;// Created by xuanyuli on 4/8/18.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        if(name.length() > 20){
          name = name.substring(0, 20);
        }
        List<String> temp = new ArrayList<>();
        if(tagsMap.containsKey(name)){
          temp = tagsMap.get(name);
        }
        temp.add(ss[1]);
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
    if(name.length() > 20){
      name = name.substring(0, 20);
    }
    System.out.println("Input is-------" + name);
    List<String> result = tagsMap.get(name);
    while(result.size() > want){
      result.remove(result.size() - 1);
    }
    return result;
  }
}

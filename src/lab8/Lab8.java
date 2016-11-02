package lab8;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Lab8 {
    private static HashMap<String, ArrayList<String>>mapPredecessor = new HashMap<>(); 
    private static HashMap<String, Integer> mapTime = new HashMap<>();
    private static ArrayList<String[]> Lines = new ArrayList<>();
    private static HashMap<String, Integer> mapcompletionTime = new HashMap<>();
    private static HashMap<String, Boolean> path = new HashMap<>();
    private static void breakIntoValues() throws FileNotFoundException {
        Scanner scan = new Scanner(new File("input.txt"));
        String nextLine = scan.nextLine();           
        while(scan.hasNext()) {         
            String s = scan.nextLine();
            String delimiter = "\\s+|,\\s*";
            String[] temp = s.split(delimiter);
            Lines.add(temp);
            mapTime.put(temp[0],Integer.valueOf(temp[temp.length-1]));
            ArrayList<String> tArrayList = new ArrayList<>();
            for(int i = 1; i < temp.length-1; ++i) {
              tArrayList.add(temp[i]);
            }
            mapPredecessor.put(temp[0],tArrayList);
        }
    }

    /**
     *
     * @param args
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException  {
        // TODO code application logic here
        breakIntoValues();
        calculateCriticalPath();
        printPath();
    }
    
    private static void calculateCriticalPath() {
        int size = mapTime.size();
        for(HashMap.Entry<String, ArrayList<String>> entry: mapPredecessor.entrySet()) {
            ArrayList<String> temp = new ArrayList<>(entry.getValue());           
            helperCalculateCompletionTime(entry.getKey());
        }
        Integer max = Integer.MIN_VALUE;
        String end = "";
        for(HashMap.Entry<String, Integer> entry: mapcompletionTime.entrySet()) {
            if(entry.getValue() > max) {           
               max = entry.getValue();
               end = entry.getKey();
            }
        }
        path.put(end,true);
        while(!mapPredecessor.get(end).get(0).equals("-")) {
            ArrayList<String> list = new ArrayList<>(mapPredecessor.get(end));
            max = 0;
            for(int i = 0; i < list.size(); ++i) {
                if(max < mapcompletionTime.get(list.get(i))) {
                    max = mapcompletionTime.get(list.get(i));
                    end = list.get(i);
                }                
            }
            path.put(end, true);
        }
    }

    private static void helperCalculateCompletionTime(String temp) {
          if(mapPredecessor.get(temp).get(0).equals("-")) {
              mapcompletionTime.put(temp, mapTime.get(temp));
         //     System.out.println(temp + " " + mapTime.get(temp));
              return;
          }
          else if(mapcompletionTime.containsKey(temp))
              return;
          ArrayList<String> arrList = mapPredecessor.get(temp);
          int max = 0;
          for(int i = 0; i < arrList.size(); ++i) {
              helperCalculateCompletionTime(arrList.get(i));
              if(max < mapcompletionTime.get(arrList.get(i))) {
                  max =  mapcompletionTime.get(arrList.get(i));
              }              
          }
          mapcompletionTime.put(temp, max + mapTime.get(temp));
       //   System.out.println(temp + " " + mapcompletionTime.get(temp));
    }

    private static void printPath() {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Activity     "+"Start Time    "+"Completion Time    " + "CriticalPath");
        for(HashMap.Entry<String, Integer> entry: mapcompletionTime.entrySet()) {
           String activity = entry.getKey();
           Integer comTime = entry.getValue();
           Integer startTime = entry.getValue() - mapTime.get(entry.getKey());
           String criticalPath = "";
           if(path.containsKey(entry.getKey())) {
               criticalPath = "*";
           }
           System.out.printf("  %-12s%-16d%-16d%10s\n", activity, startTime, comTime, criticalPath);
        }
    }
    
}

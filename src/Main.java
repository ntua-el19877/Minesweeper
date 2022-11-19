package src;

import java.util.Scanner;

/*
 * Angelos Loukas
 * Minesweeper Project 
 * Texnologi polumeson
 * 18/11/22
 */   

public class Main {

 
   public static void main(String []args) throws Exception {
      int scenario_info[]=new int[6];
      scenario_info=Lib.ReadFile("SCENARIO-2");
      //for(int i=0;i<6;i++)
      //   System.out.println(scenario_info[i]);

      Lib.create_bomb_positions(scenario_info[4],scenario_info[5],scenario_info[1],scenario_info[3]);
    }

 }
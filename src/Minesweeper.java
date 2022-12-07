package src;

import java.io.IOException;
import java.lang.System.Logger.Level;

/*
 * Angelos Loukas
 * Minesweeper Project 
 * Texnologi polumeson
 * 18/11/22
 */   

public class Minesweeper {

//    public static void openFileLocation(String path) {
//       if (InfoTool.osName.toLowerCase().contains("win")) {
//           try {
//               Runtime.getRuntime().exec("explorer.exe /select," + path);
//           } catch (IOException ex) {
//               Main.logger.log(Level.WARNING, ex.getMessage(), ex);
//           }
//       }
//   }
   public static String scenario=null;
   //public static String String_name;
   public static void main(String []args) throws Exception {
      /*
       * scenario_info[0]=mode
       * scenario_info[1]=bombs
       * scenario_info[2]=time
       * scenario_info[3]=mega_bomb
       * scenario_info[4]=margin1y
       * scenario_info[5]=margin2x
       * 
       */
      //int scenario_info[]=new int[6];
      //Lib ll=new Lib();
      Lib.startnew(scenario);
      LibFX.main(args);


   }
 }
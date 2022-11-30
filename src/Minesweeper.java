package src;


/*
 * Angelos Loukas
 * Minesweeper Project 
 * Texnologi polumeson
 * 18/11/22
 */   

public class Minesweeper {

   
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
      Lib.ReadFile("SCENARIO-1");
      //for(int i=0;i<6;i++)
      //   System.out.println(scenario_info[i]);
      int[] bomb_positions=new int[2*Lib.bomb_number];  
      //System.out.println("collums:"+Lib.collums+"\nrows:"+Lib.rows);        
      //save bomb positions in file and in arr
      bomb_positions=Lib.create_bomb_positions(); 
      // for(int i=0;i<Lib.bomb_number*2;i++)
      // {
      //    System.out.println(bomb_positions[i]);
      // }  

      
      
      Lib.board =Lib.board_creator(bomb_positions);
      System.out.println(Lib.board[2]);
      Lib.print_board(Lib.board);
      
      LibFX.main(args);


   }
 }
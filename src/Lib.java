package src;

// Java Program to illustrate Reading text file aand returning array[] with scenario info
 
// Importing input output classes
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

// Main class
public class Lib {
    public static int collums,rows,game_difficulty,bomb_number,available_time,mega_bomb;
    public static int board_len,bomb_array_len;
    //board has all values of the different positions
    public static int[] board;
    //if position is not uncoverd make 0
    public static int[] positions_uncovered;
    //holds the bomb positions y x 
    public static int[] bomb_positions;
    /*
     * collums:number of collums the board has
     * rows: number of rows the board has
     * game_difficulty
     * bomb_number:number of bombs on the board
     * available_time
     * mega_bomb:1 if there is a mega bomb
     * 
     */

    
    /*returns 4 item array of text file */
    private static void read_scenario(String string)
    {
        //int scenario_numbers[]=new int[6];

        // File path is passed as parameter
        String scenario_id_file = new String("./src/SCENARIOS/" + string + ".txt");
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        try(Scanner scanner = new Scanner(new File(scenario_id_file))) {
            
            game_difficulty=scanner.nextInt();
            bomb_number=scanner.nextInt();
            available_time=scanner.nextInt();
            mega_bomb=scanner.nextInt();
            if(scanner.hasNextInt()) throw new Exception("Exception message");
            //for (int i =0;i<4;i++)
            //{
            //    scenario_numbers[i]=scanner.nextInt();
            //}
           
        }
        catch(Exception BufferedReader1)
        {
            System.out.println("InvalidDescriptionException occured while reading "+string+".txt");
        }
        //return scenario_numbers;
        
    }
    
    private static int[] read_scenario_margins(String string)
    {
        int scenario_numbers_margins[]=new int[8];
        //read SCENARIO-ID-DIFFICULTY-MARGINS to throw exceptions if needed
        String scenario_id_dif_margins_file=new String("./src/SCENARIOS/" + string + "-DIFFICULTY-MARGINS.txt");
        
        try(Scanner scanner = new Scanner(new File(scenario_id_dif_margins_file))) {
            /*
                 * num1:diff. level
                 * num2&num3: mesh size
                 * num4&num5: bombs
                 * num6&num7: time
                 * num8: mega bomb
                 */
            for (int i =0;i<8;i++)
            {
                scenario_numbers_margins[i]=scanner.nextInt();
            }
           
        }
        catch(Exception BufferedReader2)
        {
            System.out.println("InvalidDescriptionException occured while reading "+string+"-DIFFICULTY-MARGINS.txt");
        }
        return scenario_numbers_margins;
    }
    
    private static void check_scenario(int[] arr8) throws Exception
    {
        if(!(
            (game_difficulty==arr8[0])&&
            (bomb_number>=arr8[3])&&
            (bomb_number<=arr8[4])&&
            (available_time>=arr8[5])&&
            (available_time<=arr8[6])&&
            (mega_bomb==arr8[7])))
            throw new Exception("InvalidValueException");
            return;
    }
    
    // main driver method
    public static void ReadFile(String string) throws Exception
    {
          
        //int scenario_numbers[]=new int[6];
        int scenario_numbers_margins[]=new int[8];

        read_scenario(string);
        scenario_numbers_margins=read_scenario_margins(string);
        
        check_scenario(scenario_numbers_margins);

        //save margins in scenario_numbers[]
        collums=scenario_numbers_margins[1];
        rows=scenario_numbers_margins[2];
        //return scenario_numbers;
    }

    private static void clear_file(String str) throws FileNotFoundException
    {
        //clears mine.txt
        PrintWriter writer = new PrintWriter(str);
        writer.print("");
        writer.close();
    }

    //if position is occupied by bomb return false
    private static Boolean check_duplicate(int [] arr,int position,int arr_len)
    {
        for(int i=0;i<arr_len;i++)
        {
            if(arr[i]==position) return false;
        }
        return true;
    }

    //fill array whith number
    public static int[] fill(int[] arr,int num,int range)
    {
        try
        {
            for(int i=0;i<range;i++)
            {
                arr[i]=num;
            }
        }
        catch(Exception FillingArray)
        {
            System.out.println("Error filling array");
        }
        return arr;
    }

    public static int[] create_bomb_positions() throws FileNotFoundException
    {   
        //clear file i want to write in
        clear_file("./src/mine.txt");
        int range=rows*collums;
        //save positions y*collums+x for each bomb in arr
        int[] arr=new int[bomb_number];
        //fill arr with -1
        arr=fill(arr,-1,bomb_number);

        Random random = new Random();
        
        //find positions on 1d graph
        for(int i=0;i<bomb_number;i++)
        {
            int x;
            do
            {
                x = random.nextInt(range);
            }//check if number already exists
            while(!check_duplicate(arr,x,bomb_number));
            arr[i]=x;
        }
        //bomb_positions hold th position of each bomb with y and x (2d)
        bomb_positions=new int[2*bomb_number];
        for(int i=0;i<bomb_number;i++)
        {
            //write bomb positions to file mine.txt (as y x megaBomb) and to bomb_positions[] (as y x)
            int x=arr[i]%collums;
            int y=arr[i]/collums;
            bomb_positions[2*i]=y;
            bomb_positions[2*i+1]=x;
            String str=y+","+x+",";
            if(i==0 &&mega_bomb==1) str+="1\n";
            else str+="0\n";
            try {
                Files.write(
                    Paths.get("./src/mine.txt"),
                     str.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                System.out.println("Error writing on mine.txt");
                //exception handling left as an exercise for the reader
            }
        }
        return bomb_positions;
    }

    //adds 1 to all neighboring positions that are not negative
    private static int[] update_near_positions(int y,int x,int[] board)
    {
        int z1=y*collums;
        int left_p=z1+x-1,right_p=z1+x+1,up_p=z1-collums+x,down_p=z1+collums+x;
        int left_up_p=up_p-1,left_down_p=down_p-1,right_up_p=up_p+1,right_down_p=down_p+1;
        //System.out.println("Position "+(y*collums+x));
        if((x>0)&&(board[left_p]>=0) ) {board[left_p]++;}                                    //left update
        if((x<collums-1)&&(board[right_p]>=0) ) {board[right_p]++;}                          //right update
        if((y<rows-1)&&(board[down_p]>=0) ) {board[down_p]++;}                               //down update
        if((y>0)&&(board[up_p]>=0) ) {board[up_p]++;}                                        //up update
        if((x>0) && (y>0)&&(board[left_up_p]>=0) ) {board[left_up_p]++;;}                    //left up update
        if((x>0) && (y<rows-1)&&(board[left_down_p]>=0) ) {board[left_down_p]++;}           //left down update
        if((x<collums-1) && (y>0)&&(board[right_up_p]>=0) ) {board[right_up_p]++;}          //right up update 
        if((x<collums-1) && (y<rows-1)&&(board[right_down_p]>=0) ) {board[right_down_p]++;} //right down update
        return board;
    }

    //insert -1 in the bomb position (into array)
    //add 1 to all positive neighboring positions 
    private static int[] arr_insert_bombs(int[] board,int[] bomb_array)
    {
        bomb_array_len=bomb_array.length;
        for(int i=0;i<bomb_array_len;i+=2)
        {
            if(i==0) board[bomb_array[i]*collums+bomb_array[i+1]]=-2;
            else board[bomb_array[i]*collums+bomb_array[i+1]]=-1;
            //System.out.println(bomb_array[i+1]*collums+bomb_array[i]);
            board=update_near_positions(bomb_array[i],bomb_array[i+1],board);         //position i is y and position i+1 is x
        }
        return board;
    }

    /*
    creates the array or "board" that stores
    -2 for mega bomb
    -1 for bomb
    and n >=0 for touching n bombs
    */ 
    public static int[] board_creator(int[] bomb_array)
    {
        board_len=rows*collums;
        //fill array with 0= coverd
        positions_uncovered=new int[board_len];
        Lib.fill(positions_uncovered,0,board_len);
        //create the "board"
        board=new int[board_len];
        //fill board with zeros(zero=touching 0 bombs)
        Lib.fill(board,0,board_len);
        //System.out.println("BoardLen:"+board_len);
        board=arr_insert_bombs(board,bomb_array);
        return board;
    }
    public static void print_board(int[] board)
    {
        for(int i=0;i<rows;i++)
        {
            for(int j=0;j<collums;j++)
            {
                if(board[i*collums+j]<0)System.out.print(board[i*collums+j]+" ");
                else System.out.print(" "+board[i*collums+j]+" ");
            }
            System.out.print("\n");
        }
    }
    
    public static ArrayList<Integer> get_positions_that_changed(int position)
    {
        //check if this positions is already uncoverd
        ArrayList<Integer> temp_array=new ArrayList<Integer>();
        if(positions_uncovered[position]==1) 
        {
            temp_array.add(-1);
            return temp_array;
        }
        //add position to array
        temp_array.add(position);
        if(board[position]==0)
        {
            //we have added this position
            positions_uncovered[position]=1;
            //update positions up under left right and the corners
            int x=position%collums;
            int y=position/collums;
            if(x>0)
            {
                //left is clear
                temp_array.addAll(get_positions_that_changed(position-1));
                if(y>0)
                {
                    //left up is clear
                    temp_array.addAll(get_positions_that_changed(position-1-collums));
                }
                if(y<rows-1)
                {
                    //left down is clear
                    temp_array.addAll(get_positions_that_changed(position-1+collums));
                }
            }
            if(x<collums-1)
            {
                //right is clear
                temp_array.addAll(get_positions_that_changed(position+1));
                if(y>0)
                {
                    //right up is clear
                    temp_array.addAll(get_positions_that_changed(position+1-collums));
                }
                if(y<rows-1)
                {
                    //right down is clear
                    temp_array.addAll(get_positions_that_changed(position+1+collums));
                }
            }
            if(y>0)
            {
                //up is clear
                temp_array.addAll(get_positions_that_changed(position-collums));
            }
            if(y<rows-1)
            {
                //down is clear
                temp_array.addAll(get_positions_that_changed(position+collums));
            }
        }
        return (temp_array);
    
    }
    public static void startnew() throws Exception
    {
        
      Lib.ReadFile("SCENARIO-1");
      bomb_positions=new int[2*bomb_number];        
      //save bomb positions in file and in arr
      create_bomb_positions(); 
      
      board =Lib.board_creator(bomb_positions);
      //System.out.println(Lib.board[2]);
      Lib.print_board(Lib.board);
    }

}
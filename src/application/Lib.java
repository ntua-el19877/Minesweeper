package application;

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
    //holds the number of rectangles that we have uncoverd (not flags and bombs)
    public static int uncoverdRectangles=0;
  //holds the number of rectangles that we have left clicked (not flags and bombs)
    public static int clickedRectangles=0;
    //saves the time that a game is started
    private static long startTime;
    //saves the time of a game
    public static long elapsedTime;
    static File parentDir = new File("C:/Users/Aggelos/eclipse-workspace/Minesweeper");
    public static String scenario=null;
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
    private static void read_scenario(String string) throws IOException
    {
        //int scenario_numbers[]=new int[6];

        // File path is passed as parameter
        String scenario_id_file = new File(parentDir, "src/SCENARIOS/"+string).getAbsolutePath();
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        
        try(Scanner scanner = new Scanner(new File(scenario_id_file))) {
            
            game_difficulty=scanner.nextInt();
            bomb_number=scanner.nextInt();
            available_time=scanner.nextInt();
            mega_bomb=scanner.nextInt();
            if(scanner.hasNextInt()) throw new Exception("Exception : Has to many inputs");
            //for (int i =0;i<4;i++)
            //{
            //    scenario_numbers[i]=scanner.nextInt();
            //}
//            System.out.println(scenario_id_file);
//            System.out.println(game_difficulty);
        }
        catch(Exception BufferedReader1)
        {
            System.out.println("InvalidDescriptionException occured while reading "+string);
            
        }
        //return scenario_numbers;
        
    }
    
    private static int[] read_scenario_margins() throws Exception
    {
        int scenario_numbers_margins[]=new int[8];
        //read SCENARIO-ID-DIFFICULTY-MARGINS to throw exceptions if needed
        String scenario_id_dif_margins_file=new File(parentDir, "src/medialab/SCENARIO-"+ game_difficulty + "-DIFFICULTY-MARGINS.txt").getAbsolutePath();
        
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
            System.out.println("InvalidDescriptionException occured while reading SCENARIO-"+game_difficulty+"-DIFFICULTY-MARGINS.txt");
            //if any exception is thrown init SCENARIO-1
            startnew("SCENARIO-1.txt");
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
    private static void ReadFile(String string) throws Exception
    {
          
        //int scenario_numbers[]=new int[6];
        int scenario_numbers_margins[]=new int[8];

        read_scenario(string);
        scenario_numbers_margins=read_scenario_margins();
        
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

    /**
     * 
     * Fills an array with a value 
     * 
     * @param arr is the array that will be filled
     * @param num is the value with which the array will be filled
     * @param range is the number of items that will change starting from the position 0
     * @return returns the filled array
     */
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

    private static int[] create_bomb_positions() throws FileNotFoundException
    {   
        //clear file i want to write in
        clear_file(new File(parentDir,"src/mine.txt").getAbsolutePath());
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
                    Paths.get(new File(parentDir, "src/mine.txt").getAbsolutePath()),
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
            if(i==0&&mega_bomb==1) board[bomb_array[i]*collums+bomb_array[i+1]]=-2;
            else board[bomb_array[i]*collums+bomb_array[i+1]]=-1;
            //System.out.println(bomb_array[i+1]*collums+bomb_array[i]);
            board=update_near_positions(bomb_array[i],bomb_array[i+1],board);         //position i is y and position i+1 is x
        }
        return board;
    }

    
    /**
     * Reads all games played that are stored inside game_data.txt.
     * @return returns an array of 20 elements (5 games) where each game played has 4 elements.
     */
    public static String[] readGames() {
        String[] row = new String[20];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(parentDir, "src/game_data.txt").getAbsolutePath()));
            for(int k=0;k<5;k++)
            {
                
            String line = reader.readLine();
            
    
            // split the line into an array of strings
            String[] parts = line.split(" ");
            for (int i = 0; i < 4; i++) {
                row[k*4+i] = parts[i];
            }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return row;
    }

    /**
     * Inserts the last game info into game_data.txt.
     * @param gameStatus is either "Won" or "Lost"
     */
    public static void saveGame(String gameStatus) {
        String gameString=Integer.toString(bomb_number)+' '+Integer.toString(clickedRectangles)+' '+Long.toString(elapsedTime)+' '+gameStatus;
        File file = new File(parentDir,"src/game_data.txt");
        try {
            // read the existing data from the file, if it exists
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] lines = new String[5];
            int numLines = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (numLines < 5) {
                    lines[numLines] = line;
                    numLines++;
                }
            }
            reader.close();
    
            // add the new game to the array
            if (numLines < 5) {
                lines[numLines] = gameString;
                numLines++;
            } else {
                for (int i = 1; i < 5; i++) {
                    lines[i - 1] = lines[i];
                }
                lines[4] = gameString;
            }
    
            // write the updated data to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < numLines; i++) {
                writer.write(lines[i] + "\n");
            }
            writer.close();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Starts a timer.
     */
    public static void startTimer(){startTime=System.currentTimeMillis();}
    /**
     * Stops a timer.
     */
    public static void stopTimer(){elapsedTime=(int) (System.currentTimeMillis()-startTime)/1000+1;}
    /*
    creates the array or "board" that stores
    -2 for mega bomb
    -1 for bomb
    and n >=0 for touching n bombs
    */ 
    private static int[] board_creator(int[] bomb_array)
    {
    	try {

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
            
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception creating new board");
    	}
    	return board;
    }
    
    /**
     * Prints the values of the board for the user to see.<br>
     * If the printed number is:<br>
     * 		&nbsp;n>=0 then it is a position with n bombs touching it.<br>
     * 		&nbsp;n=-1 then it is a position where a bomb is.<br>
     * 		&nbsp;n=-2 then it is a position where a mega bomb lies.<br>
     * 		&nbsp;n<-10 then is is a position with a flag
     */
    public static void print_board()
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
        System.out.print("\n");
    }
    
    //returns an array of all the positions that changed with the click oc a rectangle
    
    /**
     * Finds which positions changed on a click.  
     * 
     * @param position position that has been changed
     * @return returns an ArrayList of all the positions that updated next to it
     */
    public static ArrayList<Integer> get_positions_that_changed(int position)
    {
        //check if this positions is already uncoverd
        ArrayList<Integer> temp_array=new ArrayList<Integer>();
        //if there is a flag then remove flag
        if(board[position]<-10) {
            // System.out.println(position);
            board[position]+=20;
            // System.out.println(LibFX.flag_num);
            Main.flag_num=Main.flag_num-1;
            // System.out.println(LibFX.flag_num);

        }
        //check if position is already uncoverd
        if(positions_uncovered[position]==1) 
        {
            temp_array.add(-1);
            return temp_array;
        }
        //add position to array
        temp_array.add(position);
        uncoverdRectangles++;
        //we have added this position
        positions_uncovered[position]=1;
        if(board[position]==0)
        {
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
    
    /**Initializes the new game variables from the scenario that is chosen
     * 
     * @param scenario is the name of the file that will be read to start a new game
     * @throws Exception Exception starting new game
     * 
     */
    public static void startnew(String scenario) throws Exception
    {
    	try {
        if(scenario==null) scenario="SCENARIO-1.txt";
        ReadFile(scenario);
        bomb_positions=new int[2*bomb_number];        
        //save bomb positions in file and in arr
        create_bomb_positions(); 
        Main.counter=0;

   	 board =board_creator(bomb_positions);
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception starting new game");
    	}
    	uncoverdRectangles=0;
        clickedRectangles=0;
    }

    /**
     * Creates a .txt file and adds info of a new scenario in it.
     */
    public static void write_file() {
        try {
            System.out.println(Main.String_name);
            File myObj = new File(parentDir,"src/SCENARIOS/"+Main.String_name+".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            
            //clears mine.txt
            PrintWriter writer = new PrintWriter(new File(parentDir,"src/SCENARIOS/"+Main.String_name+".txt"));
            writer.println(Main.String_difficulty);
            writer.println(Main.String_bombs);
            writer.println(Main.String_timer);
            //writer.println(301);
            writer.println(Main.String_has_mega_bomb);
            writer.close();
        } 
        else {
            System.out.println("File already exists.");
        }
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    
}
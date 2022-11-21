package src;

// Java Program to illustrate Reading text file aand returning array[] with scenario info
 
// Importing input output classes
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

// Main class
public class Lib {
 
    /*returns 4 item array of text file */
    private static int[] read_scenario(String string)
    {
        int scenario_numbers[]=new int[6];

        // File path is passed as parameter
        String scenario_id_file = new String("./src/SCENARIOS/" + string + ".txt");
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        try(Scanner scanner = new Scanner(new File(scenario_id_file))) {
            
            for (int i =0;i<4;i++)
            {
                scenario_numbers[i]=scanner.nextInt();
            }
           
        }
        catch(Exception BufferedReader1)
        {
            System.out.println("InvalidDescriptionException occured while reading "+string+".txt");
        }
        return scenario_numbers;
        
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
    
    private static void check_scenario(int[] arr4,int[] arr8) throws Exception
    {
        if(!(
            (arr4[0]==arr8[0])&&
            (arr4[1]>=arr8[3])&&
            (arr4[1]<=arr8[4])&&
            (arr4[2]>=arr8[5])&&
            (arr4[2]<=arr8[6])&&
            (arr4[3]==arr8[7])))
            throw new Exception("InvalidValueException");
            return;
    }
    
    // main driver method
    public static int[] ReadFile(String string) throws Exception
    {
          
        int scenario_numbers[]=new int[6];
        int scenario_numbers_margins[]=new int[8];

        scenario_numbers=read_scenario(string);
        scenario_numbers_margins=read_scenario_margins(string);
        
        check_scenario(scenario_numbers,scenario_numbers_margins);

        //save margins in scenario_numbers[]
        scenario_numbers[4]=scenario_numbers_margins[1];
        scenario_numbers[5]=scenario_numbers_margins[2];
        return scenario_numbers;
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
    private static int[] fill(int[] arr,int num,int range)
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

    public static void create_bomb_positions(int rows,int collums,int bombs,int mega_bomb) throws FileNotFoundException
    {   
        clear_file("./src/mine.txt");

        //save positions x and y for each bomb
        int range=rows*collums;
        int[] arr=new int[bombs];

        //fill arr with -1
        arr=fill(arr,-1,bombs);

        Random random = new Random();

        //find positions on 1d graph
        for(int i=0;i<bombs;i++)
        {
            int x;
            do
            {
                x = random.nextInt(range);
            }//check if number already exists
            while(!check_duplicate(arr,x,bombs));
            arr[i]=x;
        }
        for(int i=0;i<bombs;i++)
        {
            
            int x=arr[i]%collums;
            int y=arr[i]/collums;
            String str=y+","+x+",";
            if(i==0) str+="1\n";
            else str+="0\n";
            try {
                Files.write(
                    Paths.get("./src/mine.txt"),
                     str.getBytes(), StandardOpenOption.APPEND);
            }catch (IOException e) {
                //exception handling left as an exercise for the reader
            }
        }
    }

    //adds 1 to all neighboring positions that are not negative
    private static int[] update_near_positions(int y,int x,int[] arr,int collums,int rows)
    {
        int z1=y*collums;
        int left_p=z1+x-1,right_p=z1+x+1,up_p=z1-collums+x,down_p=z1+collums+x;
        int left_up_p=up_p-1,left_down_p=down_p-1,right_up_p=up_p+1,right_down_p=down_p+1;
        if((left_p>=0) && (x>=0)) arr[left_p]++;                                    //left update
        if((right_p>=0) && (x<=collums-1)) arr[right_p]++;                          //right update
        if((down_p>=0) && (y<=rows-1)) arr[down_p]++;                               //down update
        if((up_p>=0) && (y>=0)) arr[up_p]++;                                        //up update
        if((left_up_p>=0) && (x>=0) && (y>=0)) arr[left_up_p]++;                    //left up update
        if((left_down_p>=0) && (x>=0) && (y<=rows-1)) arr[left_down_p]++;           //left down update
        if((right_up_p>=0) && (x<=collums-1) && (y>=0)) arr[right_up_p]++;          //right up update 
        if((right_down_p>=0) && (x<=collums-1) && (y<=rows-1)) arr[right_down_p]++; //right down update
        return arr;
    }

    //insert -1 in the bomb position (into array)
    private static int[] arr_insert_bombs(int[] arr,int[] bomb_array,int collums,int rows)
    {
        int size=arr.length;
        for(int i=0;i<size;i+=2)
        {
            arr[bomb_array[i+1]*collums+bomb_array[i]]=-1;
            arr=update_near_positions(bomb_array[i+1],bomb_array[i],arr,collums,rows);
        }
        return arr;
    }

    /*
    creates the array or "board" that stores
    -2 for mega bomb
    -1 for bomb
    and n >=0 for touching n bombs
    */ 
    public static void board_creator(int[] bomb_array,int collums,int rows)
    {
        int value_array_len=rows*collums;
        //create the "board"
        int[] value_array=new int[value_array_len];
        //fill aray with zeros=touching 0 bombs
        Lib.fill(value_array,0,value_array_len);
        value_array=arr_insert_bombs(value_array,bomb_array,collums,rows);

    }
    

}
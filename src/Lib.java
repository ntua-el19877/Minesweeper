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

}
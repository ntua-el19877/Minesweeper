package src;

// Java Program to illustrate Reading text file aand returning array[] with scenario info
// using BufferedReader Class
 
// Importing input output classes
import java.io.*;
import java.util.Scanner;
 
// Main class
public class ReadFile {
 
    /*returns 4 item array of text file */
    private static int[] read_scenario(String string)
    {
        int scenario_numbers[]=new int[4];

        // File path is passed as parameter
        String scenario_id_file = new String("/home/angelos/Documents/JavaFiles/Minesweeper/src/"+string+".txt");
 
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
            System.out.println("Erros occured while reading "+string+".txt");
        }
        return scenario_numbers;
        
    }
    
    private static int[] read_scenario_margins(String string)
    {
        int scenario_numbers_margins[]=new int[8];
        //read SCENARIO-ID-DIFFICULTY-MARGINS to throw exceptions if needed
        String scenario_id_dif_margins_file=new String("/home/angelos/Documents/JavaFiles/Minesweeper/src/"+string+"-DIFFICULTY-MARGINS.txt");
        
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
            System.out.println("Erros occured while reading "+string+"-DIFFICULTY-MARGINS.txt");
        }
        return scenario_numbers_margins;
    }
    
    // main driver method
    public static int[] main(String string) throws Exception
    {
          
        int scenario_numbers[]=new int[4];
        int scenario_numbers_margins[]=new int[8];

        scenario_numbers=read_scenario(string);
        scenario_numbers_margins=read_scenario_margins(string);
        

        return scenario_numbers;
    }


}
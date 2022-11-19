package src;

// Java Program to illustrate Reading from FileReader
// using BufferedReader Class
 
// Importing input output classes
import java.io.*;
 
// Main class
public class ReadFile {
 
    // main driver method
    public static void main(String string) throws Exception
    {
 
        // File path is passed as parameter
        File file = new File(
            "/home/angelos/Documents/JavaFiles/Minesweeper/src/"+string);
 
        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)
 
        // Creating an object of BufferedReader class
        BufferedReader br
            = new BufferedReader(new FileReader(file));
        
        int scenario_numbers[]=new int[4];
        // Declaring a string variable
        String st;
        // Condition holds true till
        // there is character in a string

        for(int i=0;i<4;i++)
        {
            st = br.readLine();
            scenario_numbers[i] = Integer.parseInt(st);
            System.out.println(st);
        }
        
    }
}
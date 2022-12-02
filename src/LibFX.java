package src;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public final class LibFX extends Application
{
    private static int rectangle_size=17;
    private static int width1=18*Lib.collums;
    private static int heigth1=400;
    private static int height_info_menu=40;
    private static int space_between_rectangles=0;

    /*make row*collums array of rectangle photos */
    private final static Rectangle[] boardRectangle = new Rectangle[Lib.rows*Lib.collums];
    /*make array to store each row of Rectangle photos */
    private final static HBox[] boardHBox = new HBox[Lib.rows];
    private final static VBox vBox=new VBox(space_between_rectangles);

    private final Stage stage1 = new Stage();
    private final static Group root = new Group();
    private final Scene Medialab_Minesweeper=new Scene(root);
    public static int counter=0;


    public static void main(String[] args)
    {
        launch(args);
    }

    private static void stage_init(final Stage stage) throws MalformedURLException
    {
        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        /*minesweeper icon */
        Image minesweeper_icon=new Image(new File(
            ".\\icons\\minesweeper_icon.png").toURI().toURL().toExternalForm());
        stage.getIcons().add(minesweeper_icon);
    }

    //init board of collums*rows blocks
    private static void board_init()
    {
        int num=0;
        // br.setFill(new ImagePattern(new Image("./icons/empty.png")));
        for(int i=0;i<Lib.collums;i++)
        {
            boardHBox[i]=new HBox(space_between_rectangles);
            for(int k=0;k<Lib.rows;k++)
            {
                /*add 15x15 rectangle Lib.row times in rowRectangle */
                boardRectangle[num]=new Rectangle(rectangle_size,rectangle_size);
                boardRectangle[num].setFill(new ImagePattern(new Image("./icons/empty.png")));
                boardHBox[i].getChildren().addAll(boardRectangle[num]);
                num++;
            }
            vBox.getChildren().addAll(boardHBox[i]);
        }
    }

    public static void update_position(int i,String s)
    {
        boardRectangle[i].setFill(new ImagePattern(new Image("./icons/"+s+".png")));
        return;
    }

    public static void update_position(int i,int s)
    {
        boardRectangle[i].setFill(new ImagePattern(new Image("./icons/"+s+".png")));
        return;
    }

    //uncovers all bombs except position
    private static void uncover_bombs(int position)
    {
        
        for(int i=0;i<Lib.board_len;i++)
        {
            //the positions with a flag are not uncovered
            if(i!=position) 
            {
                //1:ucover all bombs with no flag
                //2:uncover all mega Bombs with no flag
                //3:replace all false placed flags
                if(Lib.board[i]==-1)    boardRectangle[i].setFill(new ImagePattern(new Image("./icons/-1.png")));
                else if(Lib.board[i]==-2)    boardRectangle[i].setFill(new ImagePattern(new Image("./icons/-2.png")));
                else if(Lib.board[i]<-3 )
                {
                    if(Lib.board[i]!=-21) 
                    {
                        if(Lib.board[i]!=-22) boardRectangle[i].setFill(new ImagePattern(new Image("./icons/-21.png")));
                    }
                }
            }
                
        }
    }

    private static void clicked_position(int i) throws Exception
    {
        //if position is already uncoverd then do nothing
        if(Lib.positions_uncovered[i]==1) return;
        if(Lib.board[i]==0)
        {
            //get all positions to change in temp_array
            ArrayList<Integer> temp_array=Lib.get_positions_that_changed(i);
            for(int k=0;k<temp_array.size();k++)
            {
                
                //arr.get(k) has position
                if(temp_array.get(k)>=0) update_position(temp_array.get(k),Lib.board[temp_array.get(k)]);
            }
            
        }
        else if(Lib.board[i]==-1)
        {
            //lost the game
            update_position(i,"red_bomb");
            Lib.positions_uncovered[i]=1;
            //game over so show all bombs
            uncover_bombs(i);
            //cant look under any other spaces
            Lib.fill(Lib.positions_uncovered,1,Lib.board_len);
            //game ended start new
           // refresh_board();
            //Lib.startnew();
            
        }
        else if(Lib.board[i]==-2)
        {
            //lost the game
            update_position(i,"red_mega_bomb");
            Lib.positions_uncovered[i]=1;
            //game over so show all bombs
            uncover_bombs(i);
            //cant look under any other spaces
            Lib.fill(Lib.positions_uncovered,1,Lib.board_len);
            //game ended start new
            //refresh_board();
            //Lib.startnew();
        }
        else
        {
            //System.out.println(" board[i]:"+Lib.board[i]);
            LibFX.update_position(i,Lib.board[i]);
            //uncover position
            Lib.positions_uncovered[i]=1;
        }
        
    }

    private static void check_if_mega_bomb(int position) throws Exception
    {
        if(Lib.mega_bomb==0) return;
        if(counter>4) return;
        if(Lib.board[position]!=-2) return;

        Lib.positions_uncovered[position]=1;
        boardRectangle[position].setFill(new ImagePattern(new Image("./icons/mega_bomb_flag.png")));
        //declares row
        int start_position_x=position/Lib.collums;
        int accurate_x_position=start_position_x*Lib.collums;
        for(int i=0;i<Lib.collums;i++)
        {
            //System.out.println("Problem:"+accurate_x_position+i);
            if(Lib.positions_uncovered[accurate_x_position+i]==0)
            {
                if(Lib.board[accurate_x_position+i]>=0)
                {
                    clicked_position(accurate_x_position+i);
                }
                else
                {
                    if(accurate_x_position+i!=position)
                    { 
                        Lib.positions_uncovered[accurate_x_position+i]=1;
                        boardRectangle[accurate_x_position+i].setFill(new ImagePattern(new Image("./icons/sure_flag.png")));
                        Lib.board[accurate_x_position+i]-=20;
                    }
                }
            }
        }
        for(int i=position%Lib.collums;i<Lib.board_len;i+=Lib.collums)
        {
            //System.out.println("Problem:"+i);
            if(Lib.positions_uncovered[i]==0)
            {
                if(Lib.board[i]>=0)
                {
                    clicked_position(i);
                }
                else
                {
                    if(i!=position)
                    { 
                        Lib.positions_uncovered[i]=1;
                        boardRectangle[i].setFill(new ImagePattern(new Image("./icons/sure_flag.png")));
                        Lib.board[i]-=20;
                    }
                }
            }

        }
    }

    //checks which positions we click and changes image accordingly
    private static void check_for_clicks()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            final int I=i;
            boardRectangle[i].setOnMouseClicked(event ->
            {
                
                if (event.getButton()==MouseButton.PRIMARY)
                {
                    //skip if position is already uncoverd
                    if(Lib.positions_uncovered[I]==1) return;
                    if(Lib.board[I]<-10) return;
                    boardRectangle[I].setFill(new ImagePattern(new Image("./icons/"+Lib.board[I]+".png")));
                    //check what to do since we clicked on position
                    try {
                        counter++;
                        //uncover position I
                        clicked_position(I);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println("Exception thrown when checking for left click");
                    }
                    return;
                }
                if ((event.getButton()==MouseButton.SECONDARY))
                {
                    //if there is no flag
                    if(Lib.board[I]>-10)
                    { 
                        //if position is already uncoverd then do nothing
                        if(Lib.positions_uncovered[I]==1) return;  
                        boardRectangle[I].setFill(new ImagePattern(new Image("./icons/flag.png")));
                        counter++;
                        //check if this one is a megabomb and uncover if counter<5
                        try {
                            check_if_mega_bomb(I);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            System.out.println("Exception thrown with mega bomb");
                        }

                        //the positions with flags have value-20 to be distinguished from the other numbers when game ends
                        Lib.board[I]-=20;
                    }
                    else
                    {     
                        if(Lib.positions_uncovered[I]==1) return;
                        counter++;
                        //add 20 since the flag is not there anymore
                        Lib.board[I]+=20;
                        boardRectangle[I].setFill(new ImagePattern(new Image("./icons/empty.png")));
                        
                    }
                }
            } );
        }
    }

    
    //must be deleted
    private static void find_mega()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            if(Lib.board[i]==-2)
            {
                boardRectangle[i].setFill(new ImagePattern(new Image("./icons/-2.png")));
            }
        }
    }
    
    private static void refresh_board()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            boardRectangle[i].setFill(new ImagePattern(new Image("./icons/empty.png")));
        }
    }

    //rectangel that has the timers bombs etc
    private static void info_rect_addition()
    {
        
        Rectangle info_rect=new Rectangle(width1-20,height_info_menu);
        //info_rect.setHeight(height_info_menu);
        info_rect.setFill(Color.RED);
        vBox.getChildren().addAll(info_rect);
        info_rect.setOnMouseClicked(event ->
            {
                refresh_board();
                if (event.getButton()==MouseButton.PRIMARY)
                { 
                    try {
                        Lib.startnew();
                        find_mega();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println("Exception thrown when creating new game");
                    }
                }
            } );
    }
    @Override
    public void start(Stage arg0) throws Exception 
    {
        /*stages: stage1
         *Scenes: Medialab_Minesweeper
         *Groups: root
         */
        try
        {
            stage_init(stage1);
        }
        catch(Exception stage_init)
        {
            System.out.println("Error initializing stage1");
        }
        info_rect_addition();
        //initializes the graphics board
        board_init();
        //checks which positions we click and update nearby positions
        check_for_clicks();

        find_mega();
       // boardRectangle.get(1).setFill(new ImagePattern(new Image("./icons/1.png")));
        root.getChildren().addAll(vBox);
        Medialab_Minesweeper.setFill(Color.web("#d6d6d6"));
        stage1.setScene(Medialab_Minesweeper);
        // System.out.println(Lib.board[1]);
        // System.out.println(Lib.board[12]);
        // System.out.println(Lib.collums);
        stage1.show();
        
    }
}


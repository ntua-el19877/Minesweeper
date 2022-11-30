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
    private static int width1=300;
    private static int heigth1=400;
    private static int space_between_rectangles=0;

    /*make row*collums array of rectangle photos */
    private final static Rectangle[] boardRectangle = new Rectangle[Lib.rows*Lib.collums];
    /*make array to store each row of Rectangle photos */
    private final static HBox[] boardHBox = new HBox[Lib.rows];
    private final static VBox vBox=new VBox(space_between_rectangles);

    private final Stage stage1 = new Stage();
    private final Group root = new Group();
    private final Scene Medialab_Minesweeper=new Scene(root);


    public static void main(String[] args)
    {
        launch(args);
    }
    
    private static void stage_init(final Stage stage) throws MalformedURLException
    {
        stage.setTitle("Minesweeper");
        stage.setWidth(width1);
        stage.setHeight(heigth1);
        stage.setResizable(false);
        /*minesweeper icon */
        Image minesweeper_icon=new Image(new File(
            ".\\icons\\minesweeper_icon.png").toURI().toURL().toExternalForm());
        stage.getIcons().add(minesweeper_icon);
    }

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


    private static void check_for_clicks(int i)
    {
        boardRectangle[i].setOnMouseClicked(event ->
        {        
            if (event.getButton()==MouseButton.PRIMARY)
            {
                //check what to do since we clicked on position
                clicked_position(i);
                return;
            }
            if ((event.getButton()==MouseButton.SECONDARY))
            {
                boardRectangle[i].setFill(new ImagePattern(new Image("./icons/flag.png")));
                first_left_click(i);
            }
        } );
            
    }

    private static void first_left_click(int i)
    {
        boardRectangle[i].setOnMouseClicked(event ->
        {
            if (event.getButton()==MouseButton.SECONDARY)
            {
                boardRectangle[i].setFill(new ImagePattern(new Image("./icons/empty.png")));
                check_for_clicks(i);
            }
        });
    }

    private static void clicked_position(int i)
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
                if(temp_array.get(k)>=0)LibFX.update_position(temp_array.get(k),Lib.board[temp_array.get(k)]);
            }
            
        }
        else if(Lib.board[i]==-1)
        {
            //lost the game
            update_position(i,"red_bomb");
            Lib.positions_uncovered[i]=1;
        }
        else if(Lib.board[i]==-2)
        {
            //lost the game
            update_position(i,"red_mega_bomb");
            Lib.positions_uncovered[i]=1;
        }
        else
        {
            //System.out.println(" board[i]:"+Lib.board[i]);
            LibFX.update_position(i,Lib.board[i]);
            //uncover position
            Lib.positions_uncovered[i]=1;
        }
        
    }

    //checks which positions we click
    private static void check_for_clicks()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            final Integer I=new Integer(i);
            boardRectangle[i].setOnMouseClicked(event ->
            {
                
                if (event.getButton()==MouseButton.PRIMARY)
                {
                    if(Lib.positions_uncovered[I]==1) return;
                    boardRectangle[I].setFill(new ImagePattern(new Image("./icons/"+Lib.board[I]+".png")));
                    //check what to do since we clicked on position
                    clicked_position(I);
                    return;
                }
                if ((event.getButton()==MouseButton.SECONDARY))
                {
                    //if position is already uncoverd then do nothing
                    if(Lib.positions_uncovered[I]==1) return;
                    boardRectangle[I].setFill(new ImagePattern(new Image("./icons/flag.png")));
                    first_left_click(I);
                }
            } );
        }
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

        //initializes the graphics board
        board_init();
        //checks which positions we click and update nearby positions
        check_for_clicks();
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


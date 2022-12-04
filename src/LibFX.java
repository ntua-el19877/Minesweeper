package src;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.events.MouseEvent;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public final class LibFX extends Application
{
    private static int rectangle_size=17;
    private static Lib lib;
    private static int space_between_rectangles=0;

    /*make row*collums array of rectangle photos */
    private static Rectangle[] boardRectangle = new Rectangle[Lib.rows*Lib.collums];
    /*make array to store each row of Rectangle photos */

    
    private final static VBox root_vBox=new VBox(space_between_rectangles);
    private final Stage stage1 = new Stage();

    private static Node node_Game;
    private final Scene Medialab_Minesweeper=new Scene(root_vBox);
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

    // //init board of collums*rows blocks
    // private static VBox node_Game()
    // {
        
    //     HBox[] boardHBox = new HBox[Lib.rows];
    //     VBox vBox=new VBox(space_between_rectangles);
    //     int num=0;
    //     // br.setFill(new ImagePattern(new Image("./icons/empty.png")));
    //     for(int i=0;i<Lib.collums;i++)
    //     {
    //         boardHBox[i]=new HBox(space_between_rectangles);
    //         for(int k=0;k<Lib.rows;k++)
    //         {
    //             /*add 15x15 rectangle Lib.row times in rowRectangle */
    //             boardRectangle[num]=new Rectangle(rectangle_size,rectangle_size);
    //             boardRectangle[num].setFill(new ImagePattern(new Image("./icons/empty.png")));
    //             boardHBox[i].getChildren().addAll(boardRectangle[num]);
    //             num++;
    //         }
    //         vBox.getChildren().addAll(boardHBox[i]);
    //     }
    //     return vBox;
    // }



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
        //if there is a flag then remove flag
        if(Lib.board[i]<-10) Lib.board[i]+=20;
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

    //make all rectangles on node_Game clickable and manage each click acordingly
    public static void check_for_clicks()
    {
        for(int i=0;i<lib.board_len;i++)
        {
            final int I=i;
            boardRectangle[i].setOnMousePressed(event ->
            {
                if(lib.positions_uncovered[I]==1) return;
                if(lib.board[I]<-10) return;
                boardRectangle[I].setFill(new ImagePattern(new Image("./icons/0.png")));

            });
            boardRectangle[i].setOnMouseReleased(event ->
            {
                
                if (event.getButton()==MouseButton.PRIMARY)
                {
                    
                    //skip if position is already uncoverd
                    if(lib.positions_uncovered[I]==1) return;
                    if(lib.board[I]<-10) return;
                    boardRectangle[I].setFill(new ImagePattern(new Image("./icons/"+lib.board[I]+".png")));
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
                    if(lib.board[I]>-10)
                    { 
                        //if position is already uncoverd then do nothing
                        if(lib.positions_uncovered[I]==1) return;  
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
                        lib.board[I]-=20;
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

    //create MenuItem:Start to start new game
    private static MenuItem menuitem_Start_new_game()
    {
        MenuItem menuitem=new MenuItem("Start");
        menuitem.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent t) {
            refresh_board();
            try 
            {
                Lib.startnew(Minesweeper.scenario);                   
                find_mega();
            } 
            catch (Exception e) 
            {
                    // TODO Auto-generated catch block
                    System.out.println("Exception thrown when creating new game");
            }
                
        }
    });
    return menuitem;
    }

    //(Frontend) init node_Game of collums*rows rectangle blocks
    private static VBox node_Game_init()
    {
        boardRectangle = new Rectangle[lib.rows*lib.collums];
        HBox[] boardHBox = new HBox[lib.rows];
        VBox vBox=new VBox(space_between_rectangles);
        int num=0;
        // br.setFill(new ImagePattern(new Image("./icons/empty.png")));
        for(int i=0;i<lib.collums;i++)
        {
            boardHBox[i]=new HBox(space_between_rectangles);
            for(int k=0;k<lib.rows;k++)
            {
                /*add 15x15 rectangle Lib.row times in rowRectangle */
                boardRectangle[num]=new Rectangle(rectangle_size,rectangle_size);
                boardRectangle[num].setFill(new ImagePattern(new Image("./icons/empty.png")));
                boardHBox[i].getChildren().addAll(boardRectangle[num]);
                num++;
            }
            vBox.getChildren().addAll(boardHBox[i]);
        }
        return vBox;
    }

    //(Frontend)removes last node_Game and replaces it 
    //with new node_Game based on the last scenario we chose
    private static void remove_and_add_node_Game(Stage stage1)
    {
        VBox root_vBox=new VBox();
        //stage1.getChildren().remove(node_Game);
        //create new lib
        try {
            //(Backend)creates new board based on scenario we chose 
            Lib.startnew(Minesweeper.scenario);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Here");
        }
        root_vBox.getChildren().addAll(node_Menu_init(stage1));
        //(Frontend) init node_Game of collums*rows rectangle blocks
        node_Game=node_Game_init();
        root_vBox.getChildren().addAll(node_Game);
        
        check_for_clicks();
        Scene scene1=new Scene(root_vBox);
       // Stage stage2=new Stage();

        stage1.setScene(scene1);
        stage1.show();
    }

    //(Frontend & Backend) init a new scenario if it is clicked
    //add all scenarios in menu_Load from where you can chose one
    private static MenuItem init_scenario(File file,Stage stage1)
    {
        //create menuitem and add it to menu_Load
        MenuItem menuitem_scenario=new MenuItem(file.getName());

        //check if scenario is clicked
        menuitem_scenario.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t)
            {
                //updates scenario name
                Minesweeper.scenario=file.getName();
                //(Frontend)removes last node_Game and replaces it 
                //with new node_Game based on the last scenario we chose
                remove_and_add_node_Game(stage1);
            }
        });
        return menuitem_scenario;
    }

    //(Frontend) Swows all scenarios and if you click on one then a new game is initialized
    public static Menu menuitem_Load_New_Game(final File folder,Stage stage1) 
    {
        Menu menu_Load=new Menu("Load");
        //show all files
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                menuitem_Load_New_Game(fileEntry,stage1);
            } 
            else 
            {
                //(Frontend & Backend) init a new scenario if it is clicked
                //add all scenarios in menu_Load from where you can chose one
                menu_Load.getItems().add(init_scenario(fileEntry,stage1));
            }
        }
        return menu_Load;
    }

    //(Frontend) init menubar_Application 
    private static Node menubar_Application(Stage stage1)
    {
        MenuBar menubar_Application=new MenuBar();
        Menu menu = new Menu("Application");
        MenuItem menuitem_Start = menuitem_Start_new_game();
        //(Frontend) Shows all scenarios and if you click on one then a new game is initialized
        Menu menu_Load = menuitem_Load_New_Game(new File("./src/SCENARIOS"),stage1);
        //MenuItem menuitem_Create = menuitem_Create_Scenario();

        
        

        menu.getItems().add(menuitem_Start);
        menu.getItems().add(menu_Load);
        //menu.getItems().add(menuitem_Create);
        menubar_Application.getMenus().add(menu);

        menu.setStyle("-fx-background-color: #d6d6d6; ");
        menubar_Application.setStyle("-fx-background-color: #d6d6d6; ");
        return menubar_Application;
    }

    //(Frontend) init menubar_Details 
    private static Node menubar_Details()
    {
        MenuBar menubar_Details=new MenuBar();
        Menu menu = new Menu("Details");
        MenuItem menuitem_Start = new MenuItem("Rounds");
        MenuItem menuitem_Load = new MenuItem("Solution");
        
        menu.getItems().add(menuitem_Start);
        menu.getItems().add(menuitem_Load);
        menubar_Details.getMenus().add(menu);
        menu.setStyle("-fx-background-color: #d6d6d6; ");
        menubar_Details.setStyle("-fx-background-color: #d6d6d6; ");
        return menubar_Details;
    }
    
    //(Frontend) init node_Menu 
    private static Node node_Menu_init(Stage stage1)
    {
        HBox hbox_menu=new HBox(0);
        //(Frontend) init menubar_Application and menubar_Details and add them to node_Menu
        hbox_menu.getChildren().addAll(menubar_Application(stage1),menubar_Details());
        return hbox_menu;
    }


    @Override
    public void start(Stage arg0) throws Exception 
    {
        /*stages: stage1
         *Scenes: Medialab_Minesweeper
         *Groups: root
         */
        //Lib.startnew(Minesweeper.scenario);
        
        try
        {
            stage_init(stage1);
        }
        catch(Exception stage_init)
        {
            System.out.println("Error initializing stage1");
        }

        

        //(Frontend) init node_Menu and add it to root
        root_vBox.getChildren().addAll(node_Menu_init(stage1));
        
        //(Frontend) init node_Game of collums*rows rectangle blocks and add it to root
        root_vBox.getChildren().addAll(node_Game_init());
        root_vBox.setStyle("-fx-background-color: #d6d6d6;");
        //make all rectangles on node_Game clickable and manage each click acordingly
        check_for_clicks();
        find_mega();
        Medialab_Minesweeper.setFill(Color.web("#d6d6d6"));
        stage1.setScene(Medialab_Minesweeper);
        stage1.show();
        
    }
}


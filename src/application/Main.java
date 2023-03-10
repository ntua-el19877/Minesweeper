

package application;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.util.Duration;

public class Main extends Application
{
    private static int rectangle_size=17;
    private static int space_between_rectangles=0;
    static File parentDir = new File(System.getProperty("user.dir"));
    static int flag_num=0;
    /*make row*collums array of rectangle photos */
    private static Rectangle[] boardRectangle = new Rectangle[Lib.rows*Lib.collums];

    //these rectangles will hold info about the info screen
    //b are for the bomb numbers
    //f are for the flag numbers
    //t are for the time
    private static Rectangle b1=new Rectangle();
    private static Rectangle b2=new Rectangle();
    private static Rectangle b3=new Rectangle();
    private static Rectangle f1=new Rectangle();
    private static Rectangle f2=new Rectangle();
    private static Rectangle f3=new Rectangle();
    private static Rectangle t1=new Rectangle();
    private static Rectangle t2=new Rectangle();
    private static Rectangle t3=new Rectangle();
    /*make array to store each row of Rectangle photos */
    
    private final static VBox root_vBox=new VBox(space_between_rectangles);
    private final static Stage Medialab_Minesweeper = new Stage();

    private static Node node_Game;
    private final Scene scene1=new Scene(root_vBox);
    
    public static int counter=0;
    static Timeline oneSecondWonder = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            updateTimeInfo(-1);
        }
    }));
    static int gameTime=0;
    public static String String_name;
    public static String String_difficulty ="1";
    public static String String_bombs="0";
    public static String String_has_mega_bomb="0";
    public static String String_timer;

    private static GridPane gp=new GridPane();
    public static void main(String[] args)
    {
        launch(args);
    }

    private static void stage_init(final Stage stage) throws MalformedURLException
    {
        stage.setTitle("Minesweeper");
        stage.setResizable(false);
        /*minesweeper icon */
        Image minesweeper_icon=new Image(new File(parentDir,
            "icons/minesweeper_icon.png").toURI().toURL().toExternalForm());
        stage.getIcons().add(minesweeper_icon);
    }

    //update position i with icon s
    private static void update_position(int i,String s)
    {
    	String imageFile = new File(parentDir, "icons/"+s+".png").getAbsolutePath();
        boardRectangle[i].setFill(new ImagePattern(new Image(imageFile)));
        return;
    }

    //update position i with icon s
    private static void update_position(int i,int s)
    {
    	String imageFile = new File(parentDir, "icons/"+s+".png").getAbsolutePath();
        boardRectangle[i].setFill(new ImagePattern(new Image(imageFile)));
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
                if(Lib.board[i]==-1)    boardRectangle[i].setFill(new ImagePattern(new Image(
                		new File(parentDir, "icons/-1.png").getAbsolutePath())));
                else if(Lib.board[i]==-2)    boardRectangle[i].setFill(new ImagePattern(new Image(
                		new File(parentDir, "icons/-2.png").getAbsolutePath())));
                else if(Lib.board[i]<-3 )
                {
                    if(Lib.board[i]!=-21) 
                    {
                        if(Lib.board[i]!=-22) boardRectangle[i].setFill(new ImagePattern(new Image(
                        		new File(parentDir, "icons/-21.png").getAbsolutePath())));
                    }
                }
            }
                
        }
    }

    //check what has been clicked and change the icon accordingly
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
                if(temp_array.get(k)>=0){
                    // update flags
                    updateFlagInfo(-2);
                     update_position(temp_array.get(k),Lib.board[temp_array.get(k)]);
                     
                }
                
                // if(Lib.board[k]<-10) updateFlagInfo(-1);
            }
            
        }
        else if(Lib.board[i]==-1)
        {
            //lost the game
            update_position(i,"red_bomb");
            Lib.positions_uncovered[i]=1;
            //game over so show all bombs
            uncover_bombs(i);
            //if game ends stop all clicks
            Lib.stopTimer();
            gameEnd("Lost");
            
        }
        else if(Lib.board[i]==-2)
        {
            //lost the game
            update_position(i,"red_mega_bomb");
            Lib.positions_uncovered[i]=1;
            //game over so show all bombs
            uncover_bombs(i);
            //if game ends stop all clicks
            Lib.stopTimer();
            gameEnd("Lost");
        }
        else
        {
            Lib.uncoverdRectangles++;
            update_position(i,Lib.board[i]);
            //uncover position
            Lib.positions_uncovered[i]=1;
        }
        //if game ends stop all clicks
        if(Lib.uncoverdRectangles==Lib.board_len-Lib.bomb_number){
            Lib.stopTimer();
            gameEnd("Won");
        }
    }

    private static void check_if_mega_bomb(int position) throws Exception
    {
        if(Lib.mega_bomb==0) return;
        if(counter>4) return;
        if(Lib.board[position]!=-2) return;

        Lib.positions_uncovered[position]=1;
        boardRectangle[position].setFill(new ImagePattern(new Image(
        		new File(parentDir, "icons/mega_bomb_flag.png").getAbsolutePath())));
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
                        boardRectangle[accurate_x_position+i].setFill(new ImagePattern(new Image(new File(parentDir,"icons/sure_flag.png").getAbsolutePath())));
                        Lib.board[accurate_x_position+i]-=20;
                        updateFlagInfo(+1);
                    }
                }
            }
        }
        for(int i=position%Lib.collums;i<Lib.board_len;i+=Lib.collums)
        {
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
                        boardRectangle[i].setFill(new ImagePattern(new Image(new File(parentDir,"icons/sure_flag.png").getAbsolutePath())));
                        Lib.board[i]-=20;
                        updateFlagInfo(+1);
                    }
                }
            }

        }
    }

    //save game ,freeze all rectangles
    private static void gameEnd(String status)
    {
    	Lib.fill(Lib.positions_uncovered, 1, Lib.board_len);
        if(Lib.elapsedTime>Lib.available_time) Lib.elapsedTime=Lib.available_time;
        Lib.saveGame(status);
        oneSecondWonder.stop();
    }
    
    //updates bomb info with
    private static void updateBombInfo()
    {
    	try
    	{ 
        		int _l=Lib.bomb_number%10;
        		int _m=(Lib.bomb_number/10)%10;
        		int _f=(Lib.bomb_number/100)%10;
        		b3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_l+".jpg").getAbsolutePath())));
        		b2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_m+".jpg").getAbsolutePath())));
        		b1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_f+".jpg").getAbsolutePath())));
            return;
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception when updating time info");
    	}
    }
    
    //updates time info with +1 and updates time info 
    private static void updateTimeInfo(int n)
    {
    	try
    	{
    		if(gameTime==0) 
    		{
    	        Lib.stopTimer();
    			gameEnd("Lost");
                return;
    		}
        	if(n==0){ 
        		if(gameTime<0)gameTime=Lib.available_time;
        		int _l=gameTime%10;
        		int _m=(gameTime/10)%10;
        		int _f=(gameTime/100)%10;
        		t3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_l+".jpg").getAbsolutePath())));
        		t2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_m+".jpg").getAbsolutePath())));
        		t1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_f+".jpg").getAbsolutePath())));
            return;}
            gameTime--;
            //update last flag info
            int _l=gameTime%10;
            int _m=(gameTime/10)%10;
            int _f=(gameTime/100)%10;
            t3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_l+".jpg").getAbsolutePath())));
    		t2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_m+".jpg").getAbsolutePath())));
    		t1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_f+".jpg").getAbsolutePath())));

            return;
    	}
    	catch(Exception e)
    	{
    		System.out.println("Exception when updating time info");
    	}
    }

    //updates flag_num with +up and updates flag info 
    private static void updateFlagInfo(int up)
    {
        //update all to 0
        if(up==0) 
        {
            f1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer0.jpg").getAbsolutePath())));
            f2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer0.jpg").getAbsolutePath())));
            f3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer0.jpg").getAbsolutePath())));
            return;
        }
        else if(up==-2)//if -2 update position
        {
            int _l=flag_num%10;
        int _m=(flag_num/10)%10;
        int _f=(flag_num/100)%10;
        f3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_l+".jpg").getAbsolutePath())));
        f2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_m+".jpg").getAbsolutePath())));
        f1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_f+".jpg").getAbsolutePath())));
        return;
        }
        flag_num+=up;
        //update last flag info
        int _l=flag_num%10;
        int _m=(flag_num/10)%10;
        int _f=(flag_num/100)%10;
        
        f3.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_l+".jpg").getAbsolutePath())));
        //second digit changed
        if((_l==0&&up==+1)||(_l==9&&up==-1))
        {
            f2.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_m+".jpg").getAbsolutePath())));
        }
        //first digit changed
        if((_m==0&&up==+1)||(_m==9&&up==-1))
        {
            f1.setFill(new ImagePattern(new Image(new File(parentDir,"icons/timer"+_f+".jpg").getAbsolutePath())));
        }
        return;
    }

    //make all rectangles on node_Game clickable and manage each click acordingly
    private static void check_for_clicks()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            final int I=i;
            boardRectangle[i].setOnMousePressed(event ->
            {
                if(Lib.positions_uncovered[I]==1) return;
                if(Lib.board[I]<-10) return;
                boardRectangle[I].setFill(new ImagePattern(new Image(new File(parentDir,"icons/0.png").getAbsolutePath())));

            });
            boardRectangle[i].setOnMouseReleased(event ->
            {
                
                if (event.getButton()==MouseButton.PRIMARY)
                {
                    //start timer if new game
                    if(counter==0) {
                        Lib.startTimer();
                        updateTimeInfo(-1);
                        oneSecondWonder.setCycleCount(Timeline.INDEFINITE);
                        oneSecondWonder.play();
                    }
                    //skip if position is already uncoverd
                    if(Lib.positions_uncovered[I]==1) return;
                    //if there is a flag return
                    if(Lib.board[I]<-10) return;
                    //check what to do since we clicked on position
                    try {
                        counter++;
                        Lib.clickedRectangles++;
                        //check what has been clicked and change the icon accordingly
                        clicked_position(I);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println("Exception thrown when checking for left click");
                    }
                    return;
                }
                if ((event.getButton()==MouseButton.SECONDARY))
                {
                    //start timer if new game
                    if(counter==0) {
                        Lib.startTimer();
                        updateTimeInfo(-1);
                        oneSecondWonder.setCycleCount(Timeline.INDEFINITE);
                        oneSecondWonder.play();
                    }
                    //if there is no flag
                    if(Lib.board[I]>-10)
                    { 
                        //if position is already uncoverd then do nothing
                        if(Lib.positions_uncovered[I]==1) return;  
                        boardRectangle[I].setFill(new ImagePattern(new Image(new File(parentDir,"icons/flag.png").getAbsolutePath())));
                        updateFlagInfo(1);
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
                        updateFlagInfo(-1);
                        counter++;
                        //add 20 since the flag is not there anymore
                        Lib.board[I]+=20;
                        boardRectangle[I].setFill(new ImagePattern(new Image(new File(parentDir,"icons/empty.png").getAbsolutePath())));
                        
                    }
                }
            } );
        }
    }

    //for devs only
    private static void find_mega()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            if(Lib.board[i]==-2)
            {
                boardRectangle[i].setFill(new ImagePattern(new Image(new File(parentDir,"icons/-2.png").getAbsolutePath())));
            }
        }
    }
   
    //initializes all rectangles with an empty rectangle
    private static void refresh_board()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            boardRectangle[i].setFill(new ImagePattern(new Image(new File(parentDir,"icons/empty.png").getAbsolutePath())));
        }
    }

    //(Frontend&Backend) refresh board and start a new game 
    private static MenuItem menuitem_Start_new_game()
    {
        MenuItem menuitem=new MenuItem("Start");
        menuitem.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent t) {
            startNewGame();
                
        }
    });
    return menuitem;
    }
    
    //initializes everything to start new game
    private static void startNewGame()
    {
    	
    	try 
        {
            refresh_board();
            Lib.startnew(Lib.scenario);
            Lib.uncoverdRectangles=0;
            Lib.clickedRectangles=0;
            oneSecondWonder.stop();
            gameTime=Lib.available_time;
            updateTimeInfo(0);
            flag_num=0;
            updateFlagInfo(0);
        } 
        catch (Exception e) 
        {
                // TODO Auto-generated catch block
                System.out.println("Exception thrown when creating new game");
        }
    }

    //(Frontend) init node_Game of collums*rows rectangle blocks
    private static VBox node_Game_init()
    {
        
        boardRectangle = new Rectangle[Lib.rows*Lib.collums];
        HBox[] boardHBox = new HBox[Lib.rows];
        VBox vBox=new VBox(space_between_rectangles);
        int num=0;
        for(int i=0;i<Lib.rows;i++)
        {
            boardHBox[i]=new HBox(space_between_rectangles);
            for(int k=0;k<Lib.collums;k++)
            {
                /*add 15x15 rectangle Lib.row times in rowRectangle */
                boardRectangle[num]=new Rectangle(rectangle_size,rectangle_size);
                boardRectangle[num].setFill(new ImagePattern(new Image(new File(parentDir,"icons/empty.png").getAbsolutePath())));
                boardHBox[i].getChildren().addAll(boardRectangle[num]);
                num++;
            }
            vBox.getChildren().addAll(boardHBox[i]);
        }
        
        vBox.setStyle("-fx-background-color: #d6d6d6;"
                            + "-fx-border-color: #d6d6d6;"
                            + "-fx-border-width: 8;"
                            + "-fx-border-radius: 0;"
                            + "-fx-padding: 0;");
        return vBox;
    }

    //(Frontend)removes last node_Game and replaces it 
    //with new node_Game based on the last scenario we chose
    private static void remove_and_add_node_Game(Stage Medialab_Minesweeper)
    {
        VBox root_vBox=new VBox();
        //Medialab_Minesweeper.getChildren().remove(node_Game);
        //create new Lib
        try {
            //(Backend)creates new board based on scenario we chose 
            Lib.startnew(Lib.scenario);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Exception creating new board");
        }
        root_vBox.getChildren().addAll(node_Menu_init(Medialab_Minesweeper),node_Info_init());
        //(Frontend) init node_Game of collums*rows rectangle blocks
        node_Game=node_Game_init();
        root_vBox.getChildren().addAll(node_Game);
        check_for_clicks();
        Scene scene1=new Scene(root_vBox);
       // Stage stage2=new Stage();

        Medialab_Minesweeper.setScene(scene1);
        Medialab_Minesweeper.show();
    }

    //(Frontend & Backend) init a new scenario if it is clicked
    //add all scenarios in menu_Load from where you can chose one
    private static MenuItem init_scenario(File file,Stage Medialab_Minesweeper)
    {
        //create menuitem and add it to menu_Load
        MenuItem menuitem_scenario=new MenuItem(file.getName());

        //check if scenario is clicked
        menuitem_scenario.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t)
            {
                //updates scenario name
                Lib.scenario=file.getName();
                //(Frontend)removes last node_Game and replaces it 
                //with new node_Game based on the last scenario we chose
                remove_and_add_node_Game(Medialab_Minesweeper);
                oneSecondWonder.stop();
                gameTime=Lib.available_time;
                updateTimeInfo(0);
                flag_num=0;
                updateFlagInfo(0);
                
            }
        });
        return menuitem_scenario;
    }

    //(Frontend) Swows all scenarios and if you click on one then a new game is initialized
    private static Menu menuitem_Load_New_Game(final File folder,Stage Medialab_Minesweeper) 
    {
        Menu menu_Load=new Menu("Load");
        //show all files
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                menuitem_Load_New_Game(fileEntry,Medialab_Minesweeper);
                Lib.uncoverdRectangles=0;
                Lib.clickedRectangles=0;
                oneSecondWonder.stop();
                gameTime=Lib.available_time;
                updateTimeInfo(0);
                flag_num=0;
                updateFlagInfo(0);
            } 
            else 
            {
                //(Frontend & Backend) init a new scenario if it is clicked
                //add all scenarios in menu_Load from where you can chose one
                menu_Load.getItems().add(init_scenario(fileEntry,Medialab_Minesweeper));
                oneSecondWonder.stop();
                gameTime=Lib.available_time;
                updateTimeInfo(0);
                flag_num=0;
                updateFlagInfo(0);
            }
        }
        return menu_Load;
    }

    private static void configureBorder(final Region region) {
        region.setStyle("-fx-background-color: white;"
                            + "-fx-border-color: black;"
                            + "-fx-border-width: 1;"
                            + "-fx-border-radius: 6;"
                            + "-fx-padding: 6;");
    }

    private static HBox createHBox(final double spacing,final Node... children) {
        final HBox hbox = new HBox(spacing);
        hbox.getChildren().addAll(children);
        return hbox;
    }
    private static VBox createVBox(final double spacing,final Node... children) {
        final VBox vbox = new VBox(spacing);
        vbox.getChildren().addAll(children);
        return vbox;
    }

    //create TextField:scneario name
    private static TextField node_name()
    {
        final TextField tf_name=new TextField();
        tf_name.setPrefColumnCount(10);
        tf_name.setPromptText("Scenario Name");
        configureBorder(tf_name);
        tf_name.textProperty().addListener((observable, oldValue, newValue) -> {
        	String_name = newValue;
        });
        return tf_name;
    }

    private static TextField node_bombs()
    {           
        final TextField tf_bombs=new TextField();
        tf_bombs.setPrefColumnCount(10);
        tf_bombs.setPromptText("Number of Bombs");
        configureBorder(tf_bombs);
        tf_bombs.textProperty().addListener((observable, oldValue, newValue) -> {
        	String_bombs = newValue;
        });
        return tf_bombs;
    }
    
    
  //create TextFiled:scenario difficulty
    private static TextField node_difficulty()
    {           
        final TextField tf_difficulty=new TextField();
        tf_difficulty.setPrefColumnCount(10);
        tf_difficulty.setPromptText("Game Difficulty");
        configureBorder(tf_difficulty);
        tf_difficulty.textProperty().addListener((observable, oldValue, newValue) -> {
        	String_difficulty = newValue;
        });
        return tf_difficulty;
    }

    //create button:has mega bomb
    private static VBox node_has_mega_bomb()
    {
        final Text text_has_mega_bomb=new Text("Has a Mega Bomb");
        final Label acceptanceLabel = new Label("No");
        final Button acceptButton = new Button("Yes");
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                acceptanceLabel.setText("Yes");
                String_has_mega_bomb="1";
            }
        });
        final Button declineButton = new Button("No");
        declineButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                acceptanceLabel.setText("No");
                String_has_mega_bomb="0";
            }
        });
        final HBox panel = createHBox(6,acceptButton,
                                    declineButton,
                                    acceptanceLabel);
        final VBox vbox_has_mega_bomb=createVBox(6, text_has_mega_bomb,panel);
        panel.setAlignment(Pos.CENTER_LEFT);
        vbox_has_mega_bomb.setAlignment(Pos.CENTER_LEFT);
        configureBorder(vbox_has_mega_bomb);
        return vbox_has_mega_bomb;
    }

    //create button:finish
    private static Button node_finish(Stage stage_Create_Scenario)
    {
        final Button button_Finish=new Button("Finish");
        button_Finish.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t)
            {
                Lib.write_file();
                stage_Create_Scenario.hide();
                refresh_board();
                Lib.scenario=String_name+".txt";
                try 
                {
                	//start new game with created scenario
                	startNewGame();   
                	updateBombInfo();
                    //(Frontend)removes last node_Game and replaces it 
                    //with new node_Game based on the last scenario we chose
                    remove_and_add_node_Game(Medialab_Minesweeper);
                    oneSecondWonder.stop();
                    gameTime=Lib.available_time;
                    updateTimeInfo(0);
                    flag_num=0;
                    updateFlagInfo(0);
                } 
                catch (Exception e) 
                {
                    System.out.println("Exception thrown when creating new game");
                }
              
            } 
        });
            configureBorder(button_Finish);
            return button_Finish;
    }

    //create TextField:time
    private static TextField node_timer() {
        final TextField tf_timer = new TextField();
        tf_timer.setPrefColumnCount(10);
        tf_timer.setPromptText("Time");
        configureBorder(tf_timer);
        tf_timer.textProperty().addListener((observable, oldValue, newValue) -> {
            String_timer = newValue;
        });
        return tf_timer;
    }


    private static MenuItem menuitem_Create_Game()
    {
        MenuItem menuitem_Create=new MenuItem("Create");
        menuitem_Create.setOnAction(new EventHandler <ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                Stage stage_Create_Scenario=new Stage();
                Group root_Create_Scenario=new Group();
                Scene scene_Create_Scenario=new Scene(
                    root_Create_Scenario );
            
                VBox vbox_Create=createVBox(5,new Text("Game Name"),
                    node_name(),new Text("Game Difficulty"),
                    node_difficulty(),new Text("Game Bombs"),
                    node_bombs(),new Text("Game Time"),
                    node_timer(),
                    node_has_mega_bomb(),
                    node_finish(stage_Create_Scenario)); 

                vbox_Create.setStyle("-fx-background-color: #d6d6d6;"
                        + "-fx-border-color: #d6d6d6;"
                        + "-fx-border-width: 10;"
                        + "-fx-border-radius: 0;"
                        + "-fx-padding: 0;");
                stage_Create_Scenario.setResizable(false);
                stage_Create_Scenario.setTitle("Custom Scenario");
                root_Create_Scenario.getChildren().addAll(vbox_Create);
                stage_Create_Scenario.setScene(scene_Create_Scenario);
                stage_Create_Scenario.show();
            }
        });
        return menuitem_Create;
    }

    private static MenuItem menuitem_Exit()
    {
        MenuItem menuitem_Exit=new MenuItem("Exit");
        menuitem_Exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t)
            {
                System.exit(0);
            }
        });
        return menuitem_Exit;
    }

    private static Menu menuitem_dev_buttons()
    {
    	 Menu menu_Dev=new Menu("Dev");
    	 MenuItem menuitem_mega=new MenuItem("Show Mega Bomb");
    	 MenuItem menuitem_bomb=new MenuItem("Show Bombs");
    	 MenuItem menuitem_board=new MenuItem("Print Board");
    	 menuitem_mega.setOnAction(new EventHandler <ActionEvent>()
         {
             public void handle(ActionEvent t)
             {
            	 find_mega();
             }
         });
    	 menuitem_bomb.setOnAction(new EventHandler <ActionEvent>()
         {
             public void handle(ActionEvent t)
             {
            	 showAllBombs();
             }
         });
    	 menuitem_board.setOnAction(new EventHandler <ActionEvent>()
         {
             public void handle(ActionEvent t)
             {
            	 Lib.print_board();
             }
         });
         menu_Dev.getItems().addAll(menuitem_bomb,menuitem_mega,menuitem_board);

         return menu_Dev;
    }
    
    //(Frontend) init menubar_Application 
    private static Node menubar_Application(Stage Medialab_Minesweeper)
    {
    	MenuBar menubar_Application=new MenuBar();

        Menu menu = new Menu("Application");

        //(Frontend&Backend) refresh board and start a new game 
        menu.getItems().add(menuitem_Start_new_game());

        //(Frontend) Shows all scenarios and if you click on one then a new game is initialized
        menu.getItems().add(menuitem_Load_New_Game(new File(
        		new File(parentDir,"src/SCENARIOS").getAbsolutePath()),
        		Medialab_Minesweeper));
    	
    		
            //(Backend) create new scenario
            menu.getItems().add(menuitem_Create_Game());
            //end program
            menu.getItems().add(menuitem_Exit());
            menu.getItems().add(menuitem_dev_buttons());
            menubar_Application.getMenus().add(menu);

            menu.setStyle("-fx-background-color: #C3C3C3; ");
            menubar_Application.setStyle("-fx-background-color: #C3C3C3; ");
    	
        
        return menubar_Application;
    }

    //make a list from all the i*4+_position items in arr
    private static VBox listItemsToVBox(int _position,String Title,String[] arr)
    {
        Text textTitle= new Text(Title);
        Text text1= new Text(arr[_position]);
        Text text2= new Text(arr[_position+4]);
        Text text3= new Text(arr[_position+8]);
        Text text4= new Text(arr[_position+12]);
        Text text5= new Text(arr[_position+16]);
        VBox vboxList=createVBox(5,textTitle,text1,text2,text3,text4,text5 );
        return vboxList;}

    //(frontend) creates popup with Round info
    private static MenuItem menuitem_Rounds(){
        MenuItem menuitem_Rounds = new MenuItem("Rounds");

        // MenuItem menuitem_Rounds=new MenuItem("Create");
        menuitem_Rounds.setOnAction(new EventHandler <ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                Stage _stage=new Stage();
                Group _root=new Group();
                Scene _scene=new Scene(
                    _root);
                String[] arr=Lib.readGames();
                HBox hbox_Rounds=createHBox(20,
                                listItemsToVBox(0,"Bombs",arr),
                                listItemsToVBox(1,"Number of tries",arr),
                                listItemsToVBox(2,"Time",arr),
                                listItemsToVBox(3,"Game status",arr));
                hbox_Rounds.setPadding(new Insets(10));
                _stage.setResizable(false);
                _stage.setTitle("Last Rounds");
                _root.getChildren().addAll(hbox_Rounds);
                _stage.setScene(_scene);
                _stage.show();
            }
        });
        return menuitem_Rounds;

        // return menuitem_Rounds;
    }

    //uncover all rectangles
    private static void showAllRectangles()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            
                //1:ucover all bombs with no flag
                //2:uncover all mega Bombs with no flag
                //3:replace all false placed flags
                if(Lib.board[i]==-1 || Lib.board[i]==-21)    boardRectangle[i].setFill(new ImagePattern(new Image(
                		new File(parentDir,"icons/flag.png").getAbsolutePath())));
                else if(Lib.board[i]==-2|| Lib.board[i]==-22)    boardRectangle[i].setFill(new ImagePattern(new Image(
                        		new File(parentDir,"icons/mega_bomb_flag.png").getAbsolutePath())));
                else {
                    if(Lib.board[i]<-10)    Lib.board[i]+=20; 
                    boardRectangle[i].setFill(new ImagePattern(new Image(
                    		new File(parentDir,"icons/"+Lib.board[i]+".png").getAbsolutePath())));}
            
        }
    }
        //uncover all rectangles
        private static void showAllBombs()
        {
            for(int i=0;i<Lib.board_len;i++)
            {
                
                    //1:ucover all bombs with no flag
                    //2:uncover all mega Bombs with no flag
                    //3:replace all false placed flags
                    if(Lib.board[i]==-1 || Lib.board[i]==-21)    boardRectangle[i].setFill(new ImagePattern(new Image(
                    		new File(parentDir,"icons/-1.png").getAbsolutePath())));
                    else if(Lib.board[i]==-2|| Lib.board[i]==-22)    boardRectangle[i].setFill(new ImagePattern(new Image(
                    		new File(parentDir,"icons/-2.png").getAbsolutePath())));
                    
                
            }
        }

    //(frontend) show solution and add game to lost games
    private static MenuItem menuitem_Solution(){
        MenuItem menuitem_Solution = new MenuItem("Solution");
        menuitem_Solution.setOnAction(new EventHandler <ActionEvent>()
        {
            public void handle(ActionEvent t)
            {
                Lib.stopTimer();
                //if you instantly show solutions give zero time
                Lib.elapsedTime=0;
                gameEnd("Lost");
               showAllRectangles();
               
            }
        });
        return menuitem_Solution;
    }

    //(Frontend) init menubar_Details 
    private static Node menubar_Details()
    {
        MenuBar menubar_Details=new MenuBar();
        Menu menu = new Menu("Details");

        menu.getItems().add(menuitem_Rounds());
        menu.getItems().add(menuitem_Solution());
        menubar_Details.getMenus().add(menu);
        menu.setStyle("-fx-background-color: #C3C3C3; ");
        menubar_Details.setStyle("-fx-background-color: #C3C3C3; ");
        return menubar_Details;
    }
    
    //(frontend) shows info
    private static Node init_info_of(String caseR)
    {
        HBox hbox_bombs=new HBox(0);
        Rectangle pad1=new Rectangle(3,21);
        Rectangle pad2=new Rectangle(3,21);
        Rectangle pad3=new Rectangle(3,21);
        Rectangle pad4=new Rectangle(3,21);
        pad1.setFill(Color.web("#060205"));
        pad2.setFill(Color.web("#060205"));
        pad3.setFill(Color.web("#060205"));
        pad4.setFill(Color.web("#060205"));
        if(caseR=="b")hbox_bombs.getChildren().addAll(pad3,b1,pad1,b2,pad2,b3,pad4);
        else if(caseR=="f")hbox_bombs.getChildren().addAll(pad3,f1,pad1,f2,pad2,f3,pad4);
        else hbox_bombs.getChildren().addAll(pad3,t1,pad1,t2,pad2,t3,pad4);
        hbox_bombs.setPadding(new Insets(0, 8, 0, 0));
        hbox_bombs.setStyle(
                             "-fx-border-color: black;"
                            + "-fx-border-width: 2;"
                            + "-fx-border-radius: 0;"
                            + "-fx-padding: 0;");
        return hbox_bombs;
    }

    private static void numOfBombsToRectangle()
    {
        int firstR,secondR,thirdR;
        firstR=Lib.bomb_number/100;
        secondR=-firstR*10+Lib.bomb_number/10;
        thirdR=-secondR*10+Lib.bomb_number;
    	File imageFile = new File(parentDir, "icons/timer"+Integer.toString(firstR)+".jpg");
        try{
            b1=new Rectangle(11,21);
        b1.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        imageFile = new File(parentDir, "icons/timer"+Integer.toString(secondR)+".jpg");
        b2=new Rectangle(11,21);
        b2.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        imageFile = new File(parentDir, "icons/timer"+Integer.toString(thirdR)+".jpg");
        b3=new Rectangle(11,21);
        b3.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        }
        catch (Exception e)
        {
            System.out.println("Problem1 inserting images");
        }
    }
    private static void numOfFlagsToRectangle()
    {
    	
    	File imageFile = new File(parentDir, "icons/timer0.jpg");
        try{
            f1=new Rectangle(11,21);
        f1.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        f2=new Rectangle(11,21);
        f2.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        f3=new Rectangle(11,21);
        f3.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        }
        catch (Exception e)
        {
            System.out.println("Problem2 inserting images");
        }
    }
    private static void timeToRectangle()
    {
    	File imageFile = new File(parentDir, "icons/timer0.jpg");

        try{
            t1=new Rectangle(11,21);
        t1.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        t2=new Rectangle(11,21);
        t2.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        t3=new Rectangle(11,21);
        t3.setFill(new ImagePattern(new Image(imageFile.getAbsolutePath())));
        }
        catch (Exception e)
        {
            System.out.println("Problem inserting images");
        }
    }

    //(frontend) init info 
    private static Node node_Info_init()
    {
        numOfBombsToRectangle();
        numOfFlagsToRectangle();
        timeToRectangle();
        HBox hbox_info=new HBox(0);
        //(Frontend) init bomb number , flags , time
        Rectangle rec=new Rectangle(8,8);
        rec.setFill(Color.web("#d6d6d6"));
        try{hbox_info.getChildren().addAll(rec,init_info_of("b"),init_info_of("f"),init_info_of("t"));}
        catch(Exception e)
        {
            System.out.println("Exception thrown while adding children to hbox_info");
        }
        hbox_info.setStyle("-fx-background-color: #d6d6d6;"
                            + "-fx-border-color: #d6d6d6;"
                            + "-fx-border-width: 0;"
                            + "-fx-border-radius: 0;"
                            + "-fx-padding: 0;");
        return hbox_info;
    }


    //(Frontend) init node_Menu 
    private static Node node_Menu_init(Stage Medialab_Minesweeper)
    {
        HBox hbox_menu=new HBox(0);
        
        try {
        //(Frontend) init menubar_Application and menubar_Details and add them to node_Menu
        hbox_menu.getChildren().addAll(menubar_Application(Medialab_Minesweeper),menubar_Details());
        }catch(Exception e) { System.out.println("Error initializing menuBar");}
        hbox_menu.setStyle("-fx-background-color: #d6d6d6;"
                            + "-fx-border-color: #d6d6d6;"
                            + "-fx-border-width: 8;"
                            + "-fx-border-radius: 0;"
                            + "-fx-border-style: solid solid hidden solid;"
                            + "-fx-padding: 0;");
        return hbox_menu;
    }


    @Override
    public void start(Stage arg0) throws Exception 
    {
        /*stages: Medialab_Minesweeper
         *Scenes: scene1
         *Groups: root
         */
        try
        {
            stage_init(Medialab_Minesweeper);
            Lib.startnew(Lib.scenario);
            root_vBox.getChildren().addAll(gp);

            Node npInfo=node_Info_init();
            // center the node in the second row and first column
            GridPane.setHalignment(npInfo, HPos.CENTER);

            gp.add(node_Menu_init(Medialab_Minesweeper),0,0);
            gp.add(npInfo,0,1);
            gp.add(node_Game_init(),0,2);
           
            check_for_clicks();
            scene1.setFill(Color.web("#d6d6d6"));
            Medialab_Minesweeper.setScene(scene1);
            Medialab_Minesweeper.show();

            
        
        }
        catch(Exception stage_init)
        {
            System.out.println("Error initializing Medialab_Minesweeper");
        }
        
    }
}


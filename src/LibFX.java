package src;

import java.io.File;
import java.net.MalformedURLException;
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
    private static void check_for_clicks()
    {
        for(int i=0;i<Lib.board_len;i++)
        {
            final Integer I=new Integer(i);
            boardRectangle[i].setOnMouseClicked(event ->
            {
                if ((event.getButton()==MouseButton.PRIMARY))
                {
                    boardRectangle[I].setFill(new ImagePattern(new Image("./icons/"+Lib.board[I]+".png")));
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

        board_init();
        check_for_clicks();
        
       // boardRectangle.get(1).setFill(new ImagePattern(new Image("./icons/1.png")));
        root.getChildren().addAll(vBox);
        Medialab_Minesweeper.setFill(Color.web("#d6d6d6"));
        stage1.setScene(Medialab_Minesweeper);
        
        stage1.show();
        
    }
}


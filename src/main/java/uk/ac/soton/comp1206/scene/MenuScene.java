package uk.ac.soton.comp1206.scene;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.network.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {

    Multimedia multimedia= new Multimedia();

    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
//        multimedia.playBackgroundMusic("Game.wav");

    }

    /**
     * Build the menu layout
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);

        //Awful title
        var titl = new Text("TetrECS");
        titl.getStyleClass().add("title");
        var titlespace= new Text();


        var label= new Label();
        label.getStyleClass().add("label");
        var ass= new VBox();
        ass.getStyleClass().add("label");

        var title = new VBox(100000, titlespace, titl);

//        mainPane.setCenter(title);
//        var a= new VBox(80, titlespace, title);


        RotateTransition effects = new RotateTransition(new Duration(1000.0D), titl);
        effects.setFromAngle(-5);
        effects.setToAngle(5);
        effects.setAutoReverse(true);
        effects.setCycleCount(-1);
        effects.play();


        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var button = new Button("           PLAY           ");

        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var howtoplaybutton = new Button("  INSTRUCTIONS  ");
        var exit = new Button("            EXIT            ");
        var leaderboard = new Button(" LEADER-BOARD ");
        var infoBox = new VBox(50, button, howtoplaybutton, leaderboard, exit);
        var a= new VBox(200, titlespace, title, infoBox);
        var ah= new HBox(325, titlespace, a);
//        mainPane.setCenter(infoBox);
        var music= new Text("MUSIC:");
        music.getStyleClass().add("score");
        var musicbutton= new Button("ON");
        var nomusicbutton= new Button("OFF");
        var musicbox= new HBox(10,musicbutton, nomusicbutton);
        var musicboxx= new VBox(10, music, musicbox);

        mainPane.setBottom(musicboxx);
        musicbutton.setOnAction(this:: musicOn);
        nomusicbutton.setOnAction(this:: musicOff);

//        menu.setAlignment(Pos.CENTER);
//        menu.getStyleClass().add("infobox");
//        mainPane.setBottom(infoBox);

        //Bind the button action to the startGame method in the menu
        mainPane.setCenter(ah);
        button.setOnAction(this::startGame);
        howtoplaybutton.setOnAction(this::howGame);
        exit.setOnAction(this:: shutdown);
        leaderboard.setOnAction(this::leaderBoard);
        if (multimedia.getMusicEnabled()==true){
            multimedia.stop();
        }
        else {
            multimedia.playBackgroundMusic("background.mp3");
        }
    }

    private void leaderBoard(ActionEvent event) {
//        gameWindow.leaderBoard();
    }

    private void musicOff(ActionEvent event) {
        multimedia.pause();
    }

    private void musicOn(ActionEvent event) {
        multimedia.start();
    }


    private void shutdown(ActionEvent event) {
//        GameOverScene.build();
        System.exit(0);
    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                shutdownGame();
            }
        });
    }

    private void shutdownGame() {
        System.exit(0);
    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

    private void howGame(ActionEvent event) {
        gameWindow.howChallenge();
    }
    private void how(ActionEvent event){

    }

}

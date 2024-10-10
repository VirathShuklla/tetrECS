package uk.ac.soton.comp1206.scene;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.network.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class GameOverScene extends BaseScene {

    Multimedia multimedia= new Multimedia();
    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public GameOverScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Game Over Scene");
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
        menuPane.getStyleClass().add("howtoplayscene");
        root.getChildren().add(menuPane);

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);
        mainPane.getStyleClass().add("GameOver");

        var play= new Button( "Play Again");
        play.setOnAction(this:: playagain);
        play.getStyleClass().add("bigtitles");
//        play.getStyleClass().add("button");
        var gametitle= new Text("          Game  Over");
        gametitle.getStyleClass().add("GameOverTitle");
        var quit = new Button("Quit");
        quit.setOnAction(this:: shutdownGame);
        quit.getStyleClass().add("bigtitles");
        var a= new Text("  ");
        var aa= new Text("  ");
        var aaa= new Text("  ");
        var aaaa= new Text("           ");
//        quit.getStyleClass().add("");
        var box= new HBox(100,aa, play,aaaa,aaa,quit,a);
        var Vbox= new VBox(150,a,gametitle, box);
        mainPane.setCenter(Vbox);
        if (multimedia.getMusicEnabled()==true){
            multimedia.stop();
        }
        else {
            multimedia.playBackgroundMusic("gameover.mp3");
        }
    }

    private void playagain(ActionEvent event) {
        gameWindow.startChallenge();
    }

    private void shutdownGame(ActionEvent event) {
        gameWindow.startMenu();
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

}

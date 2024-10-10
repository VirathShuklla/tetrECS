package uk.ac.soton.comp1206.scene;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.network.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import javax.swing.*;
import java.awt.*;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;
//    private GamePiece gamePiece= new GamePiece();
    private Multimedia multimedia= new Multimedia();

    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());

        setupGame();

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("menu-backgrounds");
        root.getChildren().add(challengePane);

        var mainPane = new BorderPane();
        challengePane.getChildren().add(mainPane);

        var board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setCenter(board);

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);



        // Initialize UI elements
        var scoreLabel = new Text("Score: ");
        var scoreValueLabel = new Text();
        scoreValueLabel.textProperty().bind(game.scoreProperty().asString());

        var levelLabel = new Text("Level: ");
        var levelValueLabel = new Text();
        levelValueLabel.textProperty().bind(game.levelProperty().asString());

        var livesLabel = new Text("Lives: ");
        var livesValueLabel = new Text();
        livesValueLabel.textProperty().bind(game.livesProperty().asString());

        var multiplierLabel = new Text("Multiplier: ");
        var multiplierValueLabel = new Text();
        multiplierValueLabel.textProperty().bind(game.multiplierProperty().asString());

        scoreLabel.getStyleClass().add("score");
        levelLabel.getStyleClass().add("score");
        livesLabel.getStyleClass().add("score");
        multiplierLabel.getStyleClass().add("score");
        scoreValueLabel.getStyleClass().add("score");
        levelValueLabel.getStyleClass().add("score");
        livesValueLabel.getStyleClass().add("score");
        multiplierValueLabel.getStyleClass().add("score");
//        scoreLabel.getStyleClass().add("score");

        HBox scoreBox = new HBox(scoreLabel, scoreValueLabel);
        HBox levelBox = new HBox(levelLabel, levelValueLabel);
        HBox livesBox = new HBox(livesLabel, livesValueLabel);
        HBox multiplierBox = new HBox(multiplierLabel, multiplierValueLabel);

        var infoBox = new HBox(40, scoreBox, levelBox, livesBox, multiplierBox);
        infoBox.setMaxWidth(gameWindow.getWidth());
        infoBox.setMaxHeight(gameWindow.getHeight());
        mainPane.setTop(infoBox);
        if (multimedia.getMusicEnabled()==true){
            multimedia.stop();
        }
        else {
            multimedia.playBackgroundMusic("gameover.mp3");
        }

        var pieceboard = new PieceBoard(game.getCurrentPiece(),gameWindow.getWidth() / 5, gameWindow.getWidth() / 5);
        var swapboard = new PieceBoard(game.getNextPiece(),gameWindow.getWidth() / 5, gameWindow.getWidth() / 5);
        var VBOX = new VBox(100, pieceboard, swapboard);
        swapboard.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Perform the swap operation when right-clicked
                game.swapCurrentPiece();
            }
        });
        pieceboard.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                // Perform the swap operation when right-clicked
                game.rotateRight();
            }
        });
//        board.setOnRightClick((clicked) ->{
//            game.rotatePieceBoard();
//        });
//        pieceBoard.setOnBlockClick((clicked) ->{
//            game.rotateRight();
//        });
//        nextPieceBoard.setOnBlockClick((clicked) ->{
//            game.swapPiece();
//        });
        mainPane.setRight(VBOX);
        game.setNextPieceListener(((currentPiece, nextPiece) -> {
            if (currentPiece!=null){
                pieceboard.displayGamePieces(currentPiece);
            }
            if (nextPiece!= null){
                swapboard.displayGamePieces(nextPiece);
            }
        }));


    }

    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);

    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        game.start();
        getScene().setOnKeyPressed(keyEvent ->{
            if (keyEvent.getCode().toString().equals("ESCAPE")){
                logger.info("Pressing esc to exit the Instructions scene");
                gameWindow.startMenu();
                game.stopGame();

            }
        });
    }

    public class ProgressBarDemo {

        JFrame frame = new JFrame();
        JProgressBar bar = new JProgressBar(0, 100);

        ProgressBarDemo() {

            bar.setValue(0);
            bar.setBounds(0, 0, 420, 50);
            bar.setStringPainted(true);
//            bar.setFont(new Font("MV Boli",,25));
            bar.setForeground(Color.RED);
            bar.setBackground(Color.BLACK);

            frame.add(bar);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(420, 420);
            frame.setLayout(null);
            frame.setVisible(true);

            fill();
        }

        public void fill() {
            int counter = 0;

            while (counter <= 100) {

                bar.setValue(counter);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                counter += 1;
            }
            bar.setString("Done! :)");
        }
    }
}

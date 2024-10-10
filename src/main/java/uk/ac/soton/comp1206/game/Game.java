package uk.ac.soton.comp1206.game;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.util.Timer;
import uk.ac.soton.comp1206.component.GameBlock;
//import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.network.Multimedia;
import uk.ac.soton.comp1206.scene.BaseScene;
import uk.ac.soton.comp1206.ui.GameWindow;
//import uk.ac.soton.comp1206.scene.GameOverScene;
import java.util.HashSet;
import java.util.Set;
import static uk.ac.soton.comp1206.scene.BaseScene.gameWindow;

import java.util.*;


/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);
    private NextPieceListener nextPieceListener;

    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private static final IntegerProperty level = new SimpleIntegerProperty(1);
    private IntegerProperty lives = new SimpleIntegerProperty(3);
    private final IntegerProperty multiplier = new SimpleIntegerProperty(1);

//    private GameWindow gameWindow= new GameWindow(this, BaseScene.getWidth(), );
    private static int timerDelay=calculateTimerDelay();


    public  Timer timer;

    /**
     * Game Piece
     */

    private GamePiece gamePiece;

    /**
     * Multimedia
     */

    private Multimedia multimedia= new Multimedia();

    /**
     * PiecePlacementStart
     */
    private long piecePlacementStart;

    /**
     * Current Level
     */
    private  int currentLevel;

    /**
     * Current Piece
     */

    private GamePiece currentPiece;

    /**
     * Next Piece
     */

    private GamePiece nextPiece;

    /**
     * AfterPiece
     */

    Set<Integer> horizontalblocksToClear = new HashSet<>();
    Set<Integer> verticalblocksToClear = new HashSet<>();

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);
        currentPiece= spawnPiece();
        nextPiece= spawnPiece();
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();
    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");
    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();

        int y = gameBlock.getY();

        if (grid.canPlayPiece(currentPiece,x,y)){
            // Can play the piece
            logger.info("Hi {}",currentPiece);
            playPiece(currentPiece,x,y);
            logger.info(" ");
            nextPiece();

        }else {
            // cant place the piece
        }

    }

    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * This function is just to check if the lines are full
     * @param placeX placement X
     * @parm placeY placement Y
     * It is very important as it checks if the lines are filled properly and keeps a count of it.
     */
    public boolean afterPiece(GamePiece piece, int placeX, int placeY) {
        startGame();
        int numRows = grid.grid.length;
        int numCols = grid.grid[0].length;
        int linesToClear = 0;
        int[][] blocks= piece.getBlocks();

        // Check for horizontal lines
        for (int i = 0; i < numRows; i++) {
            boolean isLine = true;
            for (int j = 0; j < numCols; j++) {
                if (grid.grid[i][j].get() == 0) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) {
                linesToClear++;
                for (int j = 0; j < numCols; j++) {
                    horizontalblocksToClear.add(i);
                }

            }
        }
        // Check for vertical lines
        for (int j = 0; j < numCols; j++) {
            boolean isLine = true;
            for (int i = 0; i < numRows; i++) {
                if (grid.grid[i][j].get() == 0) {
                    isLine = false;
                    break;
                }
            }
            if (isLine) {
                linesToClear++;

                for (int i = 0; i < numRows; i++) {
                    verticalblocksToClear.add(j);
                }
            }
        }
        if (linesToClear > 0) {
            logger.info("Number of lines that need to be removed: {}", linesToClear);
            clearVerticalLines(verticalblocksToClear);
            clearHorizontalLines(horizontalblocksToClear);


            updateScore(score, linesToClear);
            return true;
        }
        else {
            logger.info("Number of lines that need to be removed: {}", linesToClear);
            updateMultiplier(false);
            return false;
        }


    }

    /**
     * It will clear the lines that are found to be full
     */
    // Function to clear lines based on block indices

    private  void clearVerticalLines(Set<Integer> blocksToClear) {
        for (Integer blockIndex : blocksToClear) {
            for (int a = 0; a <= grid.getCols() - 1; a++) { // Use Grid.getCols() for valid column range
                logger.info("verticalLines {}", blockIndex);
                grid.set(a, blockIndex, 0); // Call Grid's set method (assuming it exists)
            }
        }
        verticalblocksToClear.clear();
//        multimedia.playSoundEffect("Clear.wav");
    }

    private  void clearHorizontalLines(Set<Integer> blocksToClear) {
        for (Integer blockIndex : blocksToClear) {
            for (int a = 0; a <= grid.getRows() - 1; a++) { // Use Grid.getRows() for valid row range
                logger.info("HorizontalLines {}", blockIndex);
                grid.set(blockIndex, a, 0); // Call Grid's set method (assuming it exists)
            }
        }
//        multimedia.playSoundEffect("Clear.wav");
        horizontalblocksToClear.clear();
    }

    /**
     *Play a piece by updating the grid with the piece blocks
     * @param piece the piece to place
     * @param placeX placement X
     * @param placeY placement Y
     */
    public void playPiece(GamePiece piece, int placeX, int placeY){
        int topX = placeX -1;
        int topY = placeY -1;

        int value= piece.getValue();
        //return if we cant play the piece
        int[][] blocks= piece.getBlocks();
        if(!grid.canPlayPiece(piece,placeX,placeY)){
            return;
        }
        // 0 0 0 0 0
        // 0 0 0 0 0
        // 0 0 0 0 0
        // 0 0 0 0 0

        //Our piece to play
        // 0 1 0
        // 0 1 0
        // 0 1 0

        for (var blockX=0; blockX<blocks.length; blockX++){
            for (var blockY= 0; blockY<blocks.length; blockY++){
                //blockX and a blockY coordinate inside the blocks 3X£ array
                var blockvalue = blocks[blockX][blockY];
                if (blockvalue > 0){
                    grid.set(topX+blockX, topY + blockY, value);
                }
            }
        }
        afterPiece(piece,placeX,placeY);
    }

    /**
     * Spawn piece is very importsnt for evevrything
     */

    public  GamePiece spawnPiece(){
        var maxPiece = GamePiece.PIECES;
        Random r= new Random();
        var randomPiece =  r.nextInt(maxPiece);
//        var randomPiece = 0;
        logger.info("Picking random piece {}", randomPiece);
        var piece = GamePiece.createPiece(randomPiece);
        var selectionPiece = GamePiece.createPiece(randomPiece);
        return piece;
    }

    /**
     * Getting Current Piece
     * @return
     */
    public GamePiece getCurrentPiece() {
        return currentPiece;
    }

    /**
     * Getting and making next Piece
     */
    public void nextPiece() {
        // Move currentPiece to nextPiece
        currentPiece = spawnPiece();
        nextPiece= getNextPiece();
        nextPieceListener.nextPiece(currentPiece,nextPiece);

        // Spawn a new piece for nextPiece

    }

    public void setNextPieceListener(NextPieceListener nextPieceListener){
        this.nextPieceListener= nextPieceListener;
    }

    public GamePiece getNextPiece() {
        return nextPiece;
    }

    /**
     * StartGame
     */

    public static int getTimerDelay() {
        return timerDelay;
    }
    public  void startGame() {

        resetTimer();
        ProgressBar.startTimer();
        if (timerDelay > 0) {
            if (timer != null) {
                timer.cancel(); // Cancel the existing timer task
                timer = null; // Reset the timer
            }

            timer = new Timer();
            logger.info("Timer started");

            TimerTask gameLoopTask = new TimerTask() {
                @Override
                public void run() {
                    gameLoop();
                }
            };

            timer.schedule(gameLoopTask, timerDelay, getTimerDelay());
        } else {
            System.err.println("Invalid timer delay: " + timerDelay);
        }
    }


    public void stopGame() {
        lives.set(3);
        if (timer != null) {
            timer.cancel();
            timer= null;
        }
    }

    private static int calculateTimerDelay() {
        int delay = 15000 + (500 * level.get());
        return Math.min(delay, 20000);
    }

    private  void gameLoop() {
        loseLife(); // Handle losing a life
//        discardCurrentPiece(); // Discard current piece logic
        resetTimer(); // Reset timer with new delay
        logger.info("Gameloop");
        currentLevel++; // Increase level
        piecePlacementStart = System.currentTimeMillis(); // Reset timer start on each loop
        startGame();
    }

    private  void loseLife() {
        if (lives.get()>=2){
            lives.set(lives.get()-1);
            multimedia.playSoundEffect("lifelose.wav");
            nextPiece();

        }
        else {
            score.set(0);
            multiplier.set(1);
            lives.set(3);
            stopGame();
            Platform.runLater(() -> {
                gameWindow.startGameOver();// Call startGameOver() from the JavaFX Application Thread
            });
        }
    }

    public  void resetTimer() {
        if (timer != null) {
            timer.cancel(); // Cancel pending tasks
            timer.purge(); // Clear pending tasks from the timer's task queue
            timer = null; // Set timer to null to indicate cancellation
            ProgressBar.startTimer();
        }
    }

    /**
     * Updating level, score, multiplier
     */
    public void updateScore(IntegerProperty score, int linesToClear){
        resetTimer();
        startGame();
        if (linesToClear>1){
            logger.info("The value of score {}",  linesToClear*50- linesToClear*10);
            score.set(score.get() + linesToClear*50- (linesToClear-1)*10);
            updateLevel(score.get());
            updateMultiplier(true);

        }
        else {
            logger.info("The value of score {}",  linesToClear*50);
            score.set(score.get() + linesToClear*50);
            updateLevel(score.get());
            updateMultiplier(true);
        }
        updateLevel(score.get());
        multimedia.playSoundEffect("explode.wav");
    }

    // Method to update the multiplier
    public void updateMultiplier(boolean linesCleared) {
        if (linesCleared) {
            multiplier.set(multiplier.get() + 1);
        } else {
            multiplier.set(1);
        }
    }

    // Method to update the level based on the current score
    public void updateLevel(int score) {
        int newLevel = score / 1000; // Calculate the new level
        logger.info("New level {}", newLevel);
        level.set(newLevel); // Set the new level
    }

    //Getting value
    public IntegerProperty scoreProperty() {
        return score;
    }
    public IntegerProperty levelProperty() {
        return level;
    }
    public IntegerProperty livesProperty() {
        logger.info("livesproperty is working {}", lives);
        return lives;
    }
    public IntegerProperty multiplierProperty() {
        return multiplier;
    }

    /**
     * To rotate
     */
    public void rotateRight() {
        currentPiece.rotate(1);
        nextPieceListener.nextPiece(currentPiece,nextPiece);
        multimedia.playSoundEffect("rotate.wav");
    }

    public void swapPiece() {
        var temporary = currentPiece;
        currentPiece = nextPiece;
        nextPiece = temporary;
        nextPieceListener.nextPiece(currentPiece,nextPiece);
        multimedia.playSoundEffect("rotate.wav");
    }

    public void rotateLeft() {
        currentPiece.rotate(3);
        nextPieceListener.nextPiece(currentPiece,nextPiece);
        multimedia.playSoundEffect("rotate.wav");
    }

    public void swapCurrentPiece() {
        var temporary = currentPiece;
        currentPiece = nextPiece;
        nextPiece = temporary;
        nextPieceListener.nextPiece(currentPiece,nextPiece);
        multimedia.playSoundEffect("rotate.wav");
    }
}
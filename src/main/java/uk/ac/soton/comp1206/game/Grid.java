package uk.ac.soton.comp1206.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Grid is a model which holds the state of a game board. It is made up of a set of Integer values arranged in a 2D
 * arrow, with rows and columns.
 *
 * Each value inside the Grid is an IntegerProperty can be bound to enable modification and display of the contents of
 * the grid.
 *
 * The Grid contains functions related to modifying the model, for example, placing a piece inside the grid.
 *
 * The Grid should be linked to a GameBoard for it's display.
 */
public class Grid {
    private  static final Logger logger = LogManager.getLogger(Game.class);
    private Game game;
    /**
     * The number of columns in this grid
     */
    private final int cols;

    /**
     * The number of rows in this grid
     */
    private final int rows;

    /**
     * The grid is a 2D arrow with rows and columns of SimpleIntegerProperties.
     */
    final SimpleIntegerProperty[][] grid;

    /**
     * Create a new Grid with the specified number of columns and rows and initialise them
     * @param cols number of columns
     * @param rows number of rows
     */
    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create the grid itself
        grid = new SimpleIntegerProperty[cols][rows];

        //Add a SimpleIntegerProperty to every block in the grid
        for(var y = 0; y < rows; y++) {
            for(var x = 0; x < cols; x++) {
                grid[x][y] = new SimpleIntegerProperty(0);
            }
        }
    }


    /**
     * Get the Integer property contained inside the grid at a given row and column index. Can be used for binding.
     * @param x column
     * @param y row
     * @return the IntegerProperty at the given x and y in this grid
     */
    public IntegerProperty getGridProperty(int x, int y) {
        return grid[x][y];
    }

    /**
     * Update the value at the given x and y index within the grid
     * @param x column
     * @param y row
     * @param value the new value
     */
    public void set(int x, int y, int value) {
        grid[x][y].set(value);
    }

    /**
     * Get the value represented at the given x and y index within the grid
     * @param x column
     * @param y row
     * @return the value
     */
    public int get(int x, int y) {
        try {
            //Get the value held in the property at the x and y index provided
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e) {
            //No such index
            return -1;
        }
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
     * To check if the piece can be placed or not at the given x and y
     * @param piece the piece to play
     * @param placeX placement X
     * @parm placeY placement Y
     * @return
     */
    public boolean canPlayPiece(GamePiece piece, int placeX, int placeY){
        logger.info("Checking if we can the piece {} at {},{}", piece,placeX,placeY);
        int topX = placeX -1;
        int topY = placeY -1;
        int[][] blocks= piece.getBlocks();
        logger.info(blocks);

        for (var blockX=0; blockX<blocks.length; blockX++){
            for (var blockY= 0; blockY<blocks.length; blockY++){
                //blockX and a blockY coordinate inside the blocks 3X£ array
                var blockvalue = blocks[blockX][blockY];
                if (blockvalue > 0){
                    // check if we can place this block on out grid
                    var gridValue= get(topX+blockX,topY+ blockY);
                    if (gridValue  !=0){
                        logger.info("GridValue: {}", gridValue);
                        logger.info("Unable to place the piece because someone is in its way");
                        return false;
                    }

                }
            }
        }
        return true;
    }

    // Clean is to clean the pieceboard
    public void clean(){
        for(int coordX = 0; coordX < this.cols; coordX++){
            for(int coordY = 0; coordY < this.rows; coordY++){
                this.grid[coordX][coordY].set(0);
            }
        }
    }

}

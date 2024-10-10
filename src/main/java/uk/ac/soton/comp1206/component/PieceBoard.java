package uk.ac.soton.comp1206.component;

import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.game.Grid;

public class PieceBoard extends GameBoard{

    private static final Logger logger = LogManager.getLogger(GameBoard.class);
    public GamePiece gamePiece;
    int rows;
    int cols;
    private GamePiece nextPiece;


    public PieceBoard(GamePiece gamePiece, double width, double height) {
        super(new Grid(3,3),width, height);
        displayGamePieces(gamePiece);
    }

    /**
     * This helps us display game pieces on the piece board.
     * @param gamePiece
     */


    public void displayGamePieces(GamePiece gamePiece) {
        if(gamePiece == null){
            throw new IllegalArgumentException("GamePiece can't be null");
        } else {
            int[][] blocks = gamePiece.getBlocks();
            for(int vir = 0; vir < blocks.length; vir++){
                for(int co = 0; co < blocks[vir].length; co++){
                    if(blocks[vir][co] != 0){
                        grid.set(vir, co, gamePiece.getValue());
                    } else {
                        grid.set(vir,co,0);
                    }
                }
            }
        }

    }
}
package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

public interface NextPieceListener {

    /**
     * Called whenever a new next piece is generated by the Game.
     * @param nextPiece the newly generated GamePiece
     */
    public void nextPiece(GamePiece currentPiece,GamePiece nextPiece);
}
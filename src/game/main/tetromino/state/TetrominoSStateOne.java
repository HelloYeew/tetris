package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * First state of the S tetromino (diagram 1).
 */
public class TetrominoSStateOne implements TetrominoState, TetrominoSState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.generateBlock();
    }
}

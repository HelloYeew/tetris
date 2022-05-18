package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Second state of the S tetromino (diagram 2).
 */
public class TetrominoSStateTwo implements TetrominoState, TetrominoSState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoSStateOne());
        tetromino.generateBlock();
    }
}

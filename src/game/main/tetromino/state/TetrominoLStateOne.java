package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * First state of the L tetromino (diagram 1).
 */
public class TetrominoLStateOne implements TetrominoState, TetrominoLState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoLStateTwo());
        tetromino.generateBlock();
    }
}

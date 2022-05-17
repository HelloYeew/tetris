package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Second state of the L tetromino (diagram 2).
 */
public class TetrominoLStateTwo implements TetrominoState, TetrominoLState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoLStateThree());
        tetromino.generateBlock();
    }
}

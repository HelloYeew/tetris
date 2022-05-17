package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Third state of the L tetromino (diagram 3).
 */
public class TetrominoLStateThree implements TetrominoState, TetrominoLState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoLStateOne());
        tetromino.generateBlock();
    }
}

package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Third state of the T tetromino (diagram 3).
 */
public class TetrominoTStateThree implements TetrominoState, TetrominoTState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoTStateOne());
        tetromino.generateBlock();
    }
}

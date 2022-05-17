package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Second state of the T tetromino (diagram 2).
 */
public class TetrominoTStateTwo implements TetrominoState, TetrominoTState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoTStateThree());
        tetromino.generateBlock();
    }
}

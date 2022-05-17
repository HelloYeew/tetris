package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * First state of the T tetromino (diagram 1).
 */
public class TetrominoTStateOne implements TetrominoState, TetrominoTState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoTStateTwo());
        tetromino.generateBlock();
    }
}

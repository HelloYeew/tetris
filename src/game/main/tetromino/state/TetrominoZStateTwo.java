package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Second state of the Z tetromino (diagram 2).
 */
public class TetrominoZStateTwo implements TetrominoState, TetrominoZState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoZStateOne());
        tetromino.generateBlock();
    }
}

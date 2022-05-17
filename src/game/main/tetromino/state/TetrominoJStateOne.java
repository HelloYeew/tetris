package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * First state of the J tetromino (diagram 1).
 */
public class TetrominoJStateOne implements TetrominoState, TetrominoJState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoJStateTwo());
        tetromino.generateBlock();
    }
}

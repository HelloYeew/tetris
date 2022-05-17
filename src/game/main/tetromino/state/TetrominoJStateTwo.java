package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Second state of the J tetromino (diagram 2).
 */
public class TetrominoJStateTwo implements TetrominoState, TetrominoIState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoJStateThree());
        tetromino.generateBlock();
    }
}

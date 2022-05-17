package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Third state of the J tetromino (diagram 3).
 */
public class TetrominoJStateThree implements TetrominoState, TetrominoIState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoJStateOne());
        tetromino.generateBlock();
    }
}

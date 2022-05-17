package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * First state of the I tetromino (vertical).
 */
public class TetrominoIVerticalState implements TetrominoState, TetrominoIState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoIHorizontalState());
        tetromino.generateBlock();
    }
}

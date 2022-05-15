package game.main.tetromino.state;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;

import java.util.ArrayList;

/**
 * First and only state of the O tetromino.
 */
public class TetrominoOStateOne implements TetrominoState, TetrominoOState {
    /**
     * Rotates the Tetromino.
     *
     * @param tetromino the Tetromino to rotate
     */
    @Override
    public void rotate(Tetromino tetromino) {
        tetromino.setState(new TetrominoOStateOne());
    }
}

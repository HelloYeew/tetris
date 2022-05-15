package game.main.tetromino.state;

import game.main.tetromino.Tetromino;

/**
 * Abstract class for Tetromino states.
 * <br>
 * All named states are from the <a href="https://tetris.fandom.com/wiki/Tetromino">Tetris Wiki</a>.
 */
public interface TetrominoState {
    /**
     * Rotates the Tetromino.
     * @param tetromino the Tetromino to rotate
     */
    void rotate(Tetromino tetromino);
}

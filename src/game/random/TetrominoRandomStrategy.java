package game.random;

import game.tetromino.Tetromino;

import java.util.List;

/**
 * A strategy for choosing a random tetromino from a list of tetrominos.
 */
public interface TetrominoRandomStrategy {

    /**
     * Returns a next tetromino for insert in the board.
     * @return a next tetromino for insert in the board.
     */
    Tetromino getNextTetromino();

    /**
     * Returns a list of next tetromino for showing next tetromino.
     * @return a list of next tetromino for showing next tetromino.
     */
    List<Tetromino> getTetrominoList();
}

package game.main.strategy;

import game.main.tetromino.Tetromino;

import java.util.List;

/**
 * A strategy for control the tetromino flow in the playfield.
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

    /**
     * Sets a list of tetromino for showing next tetromino.
     * @param tetrominoList a list of tetromino for showing next tetromino.
     */
    void setTetrominoList(List<Tetromino> tetrominoList);
}

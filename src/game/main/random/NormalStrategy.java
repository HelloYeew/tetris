package game.main.random;

import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoType;

import java.util.List;

/**
 * The strategy that's not random the tetromino but get the default pool from <code>TetrominoType</code>'s static method
 * and plus the index by one when get the next tetromino.
 */
public class NormalStrategy implements TetrominoRandomStrategy {
    private int index = 0;

    /**
     * Returns a next tetromino for insert in the board.
     *
     * @return a next tetromino for insert in the board.
     */
    @Override
    public Tetromino getNextTetromino() {
        if (index >= TetrominoType.getAllTetrominosShape().size()) {
            index = 0;
        }
        return TetrominoType.getAllTetrominosShape().get(index++);
    }

    /**
     * Returns a list of next tetromino for showing next tetromino.
     *
     * @return a list of next tetromino for showing next tetromino.
     */
    @Override
    public List<Tetromino> getTetrominoList() {
        // create a new list
        List<Tetromino> tetrominoList = TetrominoType.getAllTetrominosShape();
        // slice from index to the end
        tetrominoList = tetrominoList.subList(index, tetrominoList.size());
        // add from the beginning to index
        tetrominoList.addAll(TetrominoType.getAllTetrominosShape().subList(0, index));
        return tetrominoList;
    }

    /**
     * Sets a list of tetromino for showing next tetromino.
     *
     * @param tetrominoList a list of tetromino for showing next tetromino.
     */
    @Override
    public void setTetrominoList(List<Tetromino> tetrominoList) {
        // since the list is not used in this strategy, it is not implemented
    }
}

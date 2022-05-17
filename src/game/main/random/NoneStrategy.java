package game.main.random;

import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoType;

import java.util.List;

public class NoneStrategy implements TetrominoRandomStrategy {
    private List<Tetromino> tetrominoList = TetrominoType.getAllTetrominosShape();

    /**
     * Returns a next tetromino for insert in the board.
     *
     * @return a next tetromino for insert in the board.
     */
    @Override
    public Tetromino getNextTetromino() {
        return tetrominoList.get(0);
    }

    /**
     * Returns a list of next tetromino for showing next tetromino.
     *
     * @return a list of next tetromino for showing next tetromino.
     */
    @Override
    public List<Tetromino> getTetrominoList() {
        return tetrominoList;
    }

    /**
     * Sets a list of tetromino for showing next tetromino.
     * @param tetrominoList a list of tetromino for showing next tetromino.
     */
    public void setTetrominoList(List<Tetromino> tetrominoList) {
        this.tetrominoList = tetrominoList;
    }
}

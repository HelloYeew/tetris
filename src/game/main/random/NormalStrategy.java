package game.main.random;

import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoType;

import java.util.List;

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
        return TetrominoType.getAllTetrominosShape();
    }
}

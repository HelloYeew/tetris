package game.main.random;

import game.main.math.Vector2D;
import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoO;

import java.util.ArrayList;
import java.util.List;

public class ReturnSameStrategy implements TetrominoRandomStrategy {
    /**
     * Returns a next tetromino for insert in the board.
     *
     * @return a next tetromino for insert in the board.
     */
    @Override
    public Tetromino getNextTetromino() {
        return new TetrominoO(new Vector2D(0,0));
    }

    /**
     * Returns a list of next tetromino for showing next tetromino.
     *
     * @return a list of next tetromino for showing next tetromino.
     */
    @Override
    public List<Tetromino> getTetrominoList() {
        ArrayList<Tetromino> tetrominoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tetrominoList.add(new TetrominoO(new Vector2D(0,0)));
        }
        return tetrominoList;
    }
}

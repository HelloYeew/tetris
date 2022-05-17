package game.main.random;

import game.main.tetromino.Tetromino;

import java.util.List;

/**
 * Strategy that's not create or random any tetromino but need to be set manually.
 * <br>
 * This strategy mainly use in multiplayer client where opponent's playfield need to be set manually from server.
 */
public class NoneStrategy implements TetrominoRandomStrategy {
    private List<Tetromino> tetrominoList;

    /**
     * Returns a next tetromino for insert in the board.
     *
     * @return a next tetromino for insert in the board.
     */
    @Override
    public Tetromino getNextTetromino() {
        // check tetrominoList is null or not
        if (tetrominoList == null) {
            return null;
        } else {
            return tetrominoList.get(0);
        }
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

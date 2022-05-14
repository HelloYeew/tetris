package game.random;

import game.tetromino.Tetromino;
import game.tetromino.TetrominoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Traditional tetris random strategy.
 *
 * @see <a href="https://tetris.fandom.com/wiki/Random_Generator">Tetris wiki about random generator</a>
 */
public class TraditionalRandomStrategy implements TetrominoRandomStrategy {
    private static final List<Tetromino> defaultTetrominoPool = TetrominoType.getAllTetrominosShape();

    private List<Tetromino> currentTetrominoPool;

    private List<Tetromino> nextTetrominoPool;

    public TraditionalRandomStrategy() {
        currentTetrominoPool = randomizeTetrominoPool();
        nextTetrominoPool = randomizeTetrominoPool();
    }

    /**
     * Returns a next tetromino for insert in the board.
     *
     * @return a next tetromino for insert in the board.
     */
    @Override
    public Tetromino getNextTetromino() {
        Tetromino nextTetrominoPool = currentTetrominoPool.get(0);
        currentTetrominoPool.remove(0);
        checkTetrominoPool();
        return nextTetrominoPool;
    }

    /**
     * Returns a list of next tetromino for showing next tetromino.
     *
     * @return a list of next tetromino for showing next tetromino.
     */
    @Override
    public List<Tetromino> getTetrominoList() {
        List<Tetromino> tetrominoList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tetrominoList.add(nextTetrominoPool.get(i));
        }
        return tetrominoList;
    }

    private List<Tetromino> randomizeTetrominoPool() {
        List<Tetromino> tempTetrominoPool = new java.util.ArrayList<>(defaultTetrominoPool);
        // randomize the tempTetrominoPool
        for (int i = 0; i < tempTetrominoPool.size(); i++) {
            int randomIndex = (int) (Math.random() * tempTetrominoPool.size());
            Tetromino tempTetromino = tempTetrominoPool.get(randomIndex);
            tempTetrominoPool.set(randomIndex, tempTetrominoPool.get(i));
            tempTetrominoPool.set(i, tempTetromino);
        }
        return tempTetrominoPool;
    }

    private void checkTetrominoPool() {
        if (currentTetrominoPool.size() == 0) {
            // If the currentTetrominoPool is empty, then the nextTetrominoPool is the currentTetrominoPool
            // and replace nextTetrominoPool with a new random tetromino pool.
            currentTetrominoPool = nextTetrominoPool;
            nextTetrominoPool = randomizeTetrominoPool();
        }
    }
}

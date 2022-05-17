package game.main.strategy;

import game.main.tetromino.Tetromino;
import game.main.tetromino.TetrominoType;

import java.util.ArrayList;
import java.util.List;

/**
 * Traditional tetris random strategy.
 *
 * @see <a href="https://tetris.fandom.com/wiki/Random_Generator">Tetris wiki about random generator</a>
 */
public class TraditionalRandomStrategy implements TetrominoRandomStrategy {
    /**
     * Current tetromino pool that's using to generate next tetromino.
     */
    private List<Tetromino> currentTetrominoPool;

    /**
     * Next pool that's going to be used after current pool is used up.
     */
    private List<Tetromino> nextTetrominoPool;

    public TraditionalRandomStrategy() {
        currentTetrominoPool = randomizeTetrominoPool();
        nextTetrominoPool = randomizeTetrominoPool();
    }

    /**
     * Returns a next tetromino for insert in the board.
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
     * @return a list of next tetromino for showing next tetromino.
     */
    @Override
    public List<Tetromino> getTetrominoList() {
        List<Tetromino> tetrominoList = new ArrayList<>();
        if (currentTetrominoPool.size() > 4) {
            for (int i = 0; i < 4; i++) {
                tetrominoList.add(currentTetrominoPool.get(i));
            }
        } else {
            // check that how many tetromino need to get from nextTetrominoPool
            int tetrominoNeed = 4 - currentTetrominoPool.size();
            tetrominoList.addAll(currentTetrominoPool);
            for (int i = 0; i < tetrominoNeed; i++) {
                tetrominoList.add(nextTetrominoPool.get(i));
            }
        }
        return tetrominoList;
    }

    /**
     * Sets a list of tetromino for showing next tetromino.
     * @param tetrominoList a list of tetromino for showing next tetromino.
     */
    @Override
    public void setTetrominoList(List<Tetromino> tetrominoList) {
        nextTetrominoPool = tetrominoList;
    }

    /**
     * Randomize the tetromino pool.
     * @return a random tetromino pool.
     */
    private List<Tetromino> randomizeTetrominoPool() {
        List<Tetromino> tempTetrominoPool = TetrominoType.getAllTetrominosShape();
        // randomize the tempTetrominoPool
        for (int i = 0; i < tempTetrominoPool.size(); i++) {
            int randomIndex = (int) (Math.random() * tempTetrominoPool.size());
            Tetromino tempTetromino = tempTetrominoPool.get(randomIndex);
            tempTetrominoPool.set(randomIndex, tempTetrominoPool.get(i));
            tempTetrominoPool.set(i, tempTetromino);
        }
        return tempTetrominoPool;
    }

    /**
     * Check if the current tetromino pool is empty. If it is, replace current tetromino pool with next tetromino pool.
     * and re-randomize next tetromino pool.
     */
    private void checkTetrominoPool() {
        if (currentTetrominoPool.size() == 0) {
            // If the currentTetrominoPool is empty, then the nextTetrominoPool is the currentTetrominoPool
            // and replace nextTetrominoPool with a new random tetromino pool.
            currentTetrominoPool = nextTetrominoPool;
            nextTetrominoPool = randomizeTetrominoPool();
        }
    }
}

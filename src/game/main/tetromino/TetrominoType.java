package game.main.tetromino;

import game.main.math.Vector2D;

import java.util.ArrayList;

/**
 * The type of tetromino that's available in the game.
 *
 * @see <a href="https://tetris.fandom.com/wiki/Tetromino">Tetris wiki</a>
 */
public enum TetrominoType {
    I,
    J,
    L,
    O,
    S,
    T,
    Z;

    /**
     * Get a list include all tetromino types.
     * @return A list include all tetromino types.
     */
    public static ArrayList<Tetromino> getAllTetrominosShape() {
        return new ArrayList<>() {
            {
                add(new TetrominoI(new Vector2D(0, 0)));
                add(new TetrominoJ(new Vector2D(0, 0)));
                add(new TetrominoL(new Vector2D(0, 0)));
                add(new TetrominoO(new Vector2D(0, 0)));
                add(new TetrominoS(new Vector2D(0, 0)));
                add(new TetrominoT(new Vector2D(0, 0)));
                add(new TetrominoZ(new Vector2D(0, 0)));
            }
        };
    }
}

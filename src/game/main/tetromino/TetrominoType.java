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

    public static TetrominoType convertTetrominoToType(Tetromino tetromino) {
        if (tetromino instanceof TetrominoI) {
            return I;
        } else if (tetromino instanceof TetrominoJ) {
            return J;
        } else if (tetromino instanceof TetrominoL) {
            return L;
        } else if (tetromino instanceof TetrominoO) {
            return O;
        } else if (tetromino instanceof TetrominoS) {
            return S;
        } else if (tetromino instanceof TetrominoT) {
            return T;
        } else if (tetromino instanceof TetrominoZ) {
            return Z;
        } else {
            throw new IllegalArgumentException("Invalid Tetromino");
        }
    }

    public static Tetromino convertTypeToTetromino(TetrominoType type) {
        if (type == I) {
            return new TetrominoI(new Vector2D(0, 0));
        } else if (type == J) {
            return new TetrominoJ(new Vector2D(0, 0));
        } else if (type == L) {
            return new TetrominoL(new Vector2D(0, 0));
        } else if (type == O) {
            return new TetrominoO(new Vector2D(0, 0));
        } else if (type == S) {
            return new TetrominoS(new Vector2D(0, 0));
        } else if (type == T) {
            return new TetrominoT(new Vector2D(0, 0));
        } else if (type == Z) {
            return new TetrominoZ(new Vector2D(0, 0));
        } else {
            throw new IllegalArgumentException("Invalid TetrominoType");
        }
    }
}

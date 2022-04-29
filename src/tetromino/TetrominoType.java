package tetromino;

import math.Vector2D;

import java.util.Random;

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
     * Gets a random tetromino.
     * @param position The position of the tetromino that's need in tetromino's constructor.
     * @return A random tetromino.
     */
    public static Tetromino getRandomTetromino(Vector2D position) {
        // TODO: This method is not suppose to create a new tetromino every time. It use 'change' the current tetromino instead. (Object pooling)
        Random random = new Random();
        TetrominoType type = TetrominoType.values()[random.nextInt(TetrominoType.values().length)];
        return switch (type) {
            case I -> new TetrominoI(position);
            case J -> new TetrominoJ(position);
            case L -> new TetrominoL(position);
            case O -> new TetrominoO(position);
            case S -> new TetrominoS(position);
            case T -> new TetrominoT(position);
            case Z -> new TetrominoZ(position);
            default -> throw new IllegalArgumentException("Unknown TetrominoType: " + type);
        };
    }
}

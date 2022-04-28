package tetromino;

import math.Vector2D;

import java.util.Random;

/**
 * The type of tetromino that's available in the game.
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
//        TetrominoType type = TetrominoType.values()[random.nextInt(TetrominoType.values().length)];
        // TODO: This must random when we already implement all tetromino types
        TetrominoType type = I;
        switch (type) {
            case I:
                return new TetrominoI(position);
            default:
                throw new IllegalArgumentException("Unknown TetrominoType: " + type);
        }
    }
}

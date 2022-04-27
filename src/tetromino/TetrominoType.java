package tetromino;

import math.Vector2D;

import java.util.Random;

public enum TetrominoType {
    I,
    J,
    L,
    O,
    S,
    T,
    Z;

    public static Tetromino getRandomTetromino(Vector2D position) {
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

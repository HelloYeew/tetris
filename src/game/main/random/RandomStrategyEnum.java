package game.main.random;

public enum RandomStrategyEnum {
    Traditional,
    Normal,
    WeLoveO,
    None;

    public static TetrominoRandomStrategy convertToClass(RandomStrategyEnum strategy) {
        return switch (strategy) {
            case Traditional -> new TraditionalRandomStrategy();
            case Normal -> new NormalStrategy();
            case WeLoveO -> new WeLoveOStrategy();
            case None -> new NoneStrategy();
        };
    }
}

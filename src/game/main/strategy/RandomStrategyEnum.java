package game.main.strategy;

/**
 * The enum value that's using when server want send random strategy to client.
 */
public enum RandomStrategyEnum {
    Traditional,
    Normal,
    WeLoveO,
    None;

    /**
     * Convert enum value of the strategy to its object.
     * @param strategy the strategy enum value.
     * @return the object of the strategy.
     */
    public static TetrominoRandomStrategy convertToClass(RandomStrategyEnum strategy) {
        return switch (strategy) {
            case Traditional -> new TraditionalRandomStrategy();
            case Normal -> new NormalStrategy();
            case WeLoveO -> new WeLoveOStrategy();
            case None -> new NoneStrategy();
        };
    }
}

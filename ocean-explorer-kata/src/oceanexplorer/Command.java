package oceanexplorer;

/**
 * Supported control commands for the probe.
 */
public enum Command {
    FORWARD('F'),
    BACKWARD('B'),
    LEFT('L'),
    RIGHT('R');

    private final char symbol;

    Command(char symbol) {
        this.symbol = symbol;
    }

    public static Command from(char rawSymbol) {
        char normalized = Character.toUpperCase(rawSymbol);
        for (Command command : values()) {
            if (command.symbol == normalized) {
                return command;
            }
        }

        throw new IllegalArgumentException("Unsupported command: '" + rawSymbol + "'");
    }
}

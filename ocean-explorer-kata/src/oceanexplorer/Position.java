package oceanexplorer;

import java.util.Objects;

/**
 * Immutable value object representing a location on the ocean floor grid.
 */
public record Position(int x, int y) {

    public Position {
        // record constructor kept intentionally simple; grid bounds are validated by Grid
    }

    public Position translate(int deltaX, int deltaY) {
        return new Position(x + deltaX, y + deltaY);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

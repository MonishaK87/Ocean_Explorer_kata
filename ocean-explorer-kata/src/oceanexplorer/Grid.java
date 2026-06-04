package oceanexplorer;

/**
 * Bounded rectangular grid with inclusive coordinates from (0,0) to (maxX,maxY).
 */
public final class Grid {
    private final int maxX;
    private final int maxY;

    public Grid(int maxX, int maxY) {
        if (maxX < 0 || maxY < 0) {
            throw new IllegalArgumentException("Grid bounds must be zero or positive.");
        }

        this.maxX = maxX;
        this.maxY = maxY;
    }

    public boolean contains(Position position) {
        return position.x() >= 0
                && position.x() <= maxX
                && position.y() >= 0
                && position.y() <= maxY;
    }

    public int maxX() {
        return maxX;
    }

    public int maxY() {
        return maxY;
    }
}

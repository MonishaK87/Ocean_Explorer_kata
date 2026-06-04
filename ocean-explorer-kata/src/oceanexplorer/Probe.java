package oceanexplorer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Mutable aggregate representing the probe state during navigation.
 */
public final class Probe {
    private Position position;
    private Direction direction;
    private final List<Position> visitedPositions;

    public Probe(Position startingPosition, Direction startingDirection) {
        this.position = Objects.requireNonNull(startingPosition, "Starting position must not be null.");
        this.direction = Objects.requireNonNull(startingDirection, "Starting direction must not be null.");
        this.visitedPositions = new ArrayList<>();
        this.visitedPositions.add(startingPosition);
    }

    public Position position() {
        return position;
    }

    public Direction direction() {
        return direction;
    }

    public void turnLeft() {
        direction = direction.turnLeft();
    }

    public void turnRight() {
        direction = direction.turnRight();
    }

    public Position previewForwardMove() {
        return direction.moveForward(position);
    }

    public Position previewBackwardMove() {
        return direction.moveBackward(position);
    }

    public void moveTo(Position newPosition) {
        position = Objects.requireNonNull(newPosition, "New position must not be null.");
        visitedPositions.add(newPosition);
    }

    public List<Position> visitedPositions() {
        return Collections.unmodifiableList(visitedPositions);
    }
}

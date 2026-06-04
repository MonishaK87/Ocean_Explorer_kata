package oceanexplorer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Application service that validates input and applies a command stream to the probe.
 *
 * Assumptions:
 * - The grid is bounded and does not wrap.
 * - Coordinates are zero-based and inclusive, from (0,0) to (maxX,maxY).
 * - Valid commands are F, B, L, and R.
 * - Execution stops at the first obstacle or boundary block.
 * - The starting position is included in the visited path.
 */
public final class OceanExplorerController {
    private final Grid grid;
    private final Set<Position> obstacles;

    public OceanExplorerController(Grid grid, Set<Position> obstacles) {
        this.grid = Objects.requireNonNull(grid, "Grid must not be null.");
        this.obstacles = obstacles == null ? Set.of() : Set.copyOf(new HashSet<>(obstacles));

        for (Position obstacle : this.obstacles) {
            if (!grid.contains(obstacle)) {
                throw new IllegalArgumentException("Obstacle is outside the grid: " + obstacle);
            }
        }
    }

    public ExecutionResult execute(Position start, Direction startDirection, String rawCommands) {
        validateStart(start, startDirection, rawCommands);

        Probe probe = new Probe(start, startDirection);

        for (char rawCommand : rawCommands.toCharArray()) {
            Command command = Command.from(rawCommand);

            switch (command) {
                case LEFT -> probe.turnLeft();
                case RIGHT -> probe.turnRight();
                case FORWARD -> {
                    ExecutionResult blocked = attemptMove(probe, probe.previewForwardMove());
                    if (blocked != null) {
                        return blocked;
                    }
                }
                case BACKWARD -> {
                    ExecutionResult blocked = attemptMove(probe, probe.previewBackwardMove());
                    if (blocked != null) {
                        return blocked;
                    }
                }
            }
        }

        return ExecutionResult.success(probe);
    }

    private void validateStart(Position start, Direction startDirection, String rawCommands) {
        Objects.requireNonNull(start, "Starting position must not be null.");
        Objects.requireNonNull(startDirection, "Starting direction must not be null.");
        Objects.requireNonNull(rawCommands, "Command sequence must not be null.");

        if (!grid.contains(start)) {
            throw new IllegalArgumentException("Starting position is outside the grid: " + start);
        }

        if (obstacles.contains(start)) {
            throw new IllegalArgumentException("Starting position cannot contain an obstacle: " + start);
        }

        for (char rawCommand : rawCommands.toCharArray()) {
            Command.from(rawCommand);
        }
    }

    private ExecutionResult attemptMove(Probe probe, Position targetPosition) {
        if (!grid.contains(targetPosition)) {
            return ExecutionResult.blocked(
                    probe,
                    ExecutionStatus.BLOCKED_BY_BOUNDARY,
                    targetPosition,
                    "Movement blocked by grid boundary at " + targetPosition + ".");
        }

        if (obstacles.contains(targetPosition)) {
            return ExecutionResult.blocked(
                    probe,
                    ExecutionStatus.BLOCKED_BY_OBSTACLE,
                    targetPosition,
                    "Movement blocked by obstacle at " + targetPosition + ".");
        }

        probe.moveTo(targetPosition);
        return null;
    }
}

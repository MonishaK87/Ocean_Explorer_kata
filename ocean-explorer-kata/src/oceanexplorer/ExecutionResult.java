package oceanexplorer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Rich result returned after processing a command sequence.
 */
public final class ExecutionResult {
    private final Position finalPosition;
    private final Direction finalDirection;
    private final List<Position> visitedPositions;
    private final ExecutionStatus status;
    private final Position blockedPosition;
    private final String message;

    private ExecutionResult(
            Position finalPosition,
            Direction finalDirection,
            List<Position> visitedPositions,
            ExecutionStatus status,
            Position blockedPosition,
            String message) {
        this.finalPosition = Objects.requireNonNull(finalPosition, "Final position must not be null.");
        this.finalDirection = Objects.requireNonNull(finalDirection, "Final direction must not be null.");
        this.visitedPositions = List.copyOf(visitedPositions);
        this.status = Objects.requireNonNull(status, "Status must not be null.");
        this.blockedPosition = blockedPosition;
        this.message = Objects.requireNonNull(message, "Message must not be null.");
    }

    public static ExecutionResult success(Probe probe) {
        return new ExecutionResult(
                probe.position(),
                probe.direction(),
                probe.visitedPositions(),
                ExecutionStatus.SUCCESS,
                null,
                "Command sequence executed successfully.");
    }

    public static ExecutionResult blocked(Probe probe, ExecutionStatus status, Position blockedPosition, String message) {
        return new ExecutionResult(
                probe.position(),
                probe.direction(),
                probe.visitedPositions(),
                status,
                blockedPosition,
                message);
    }

    public Position finalPosition() {
        return finalPosition;
    }

    public Direction finalDirection() {
        return finalDirection;
    }

    public List<Position> visitedPositions() {
        return visitedPositions;
    }

    public ExecutionStatus status() {
        return status;
    }

    public Optional<Position> blockedPosition() {
        return Optional.ofNullable(blockedPosition);
    }

    public String message() {
        return message;
    }

    public String visitedSummary() {
        return visitedPositions.stream()
                .map(Position::toString)
                .collect(Collectors.joining(" -> "));
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "finalPosition=" + finalPosition +
                ", finalDirection=" + finalDirection +
                ", status=" + status +
                ", blockedPosition=" + blockedPosition +
                ", visitedPositions=" + visitedSummary() +
                ", message='" + message + '\'' +
                '}';
    }
}

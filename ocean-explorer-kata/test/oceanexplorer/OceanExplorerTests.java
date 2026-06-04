package oceanexplorer;

import java.util.List;
import java.util.Set;

/**
 * Lightweight dependency-free test suite.
 * Run with: java -cp out oceanexplorer.OceanExplorerTests
 */
public final class OceanExplorerTests {
    private OceanExplorerTests() {
    }

    public static void main(String[] args) {
        shouldTurnLeftFromNorthToWest();
        shouldTurnRightFromNorthToEast();
        shouldMoveForwardWhenFacingNorth();
        shouldMoveBackwardWhenFacingNorth();
        shouldExecuteMultipleCommandsInSequence();
        shouldStopAtBoundary();
        shouldStopAtObstacle();
        shouldIncludeStartingPositionInVisitedPath();
        shouldKeepOnlyStartingPositionForTurnOnlyCommands();
        shouldRejectInvalidCommand();
        shouldRejectStartingPositionOutsideGrid();
        shouldRejectStartingPositionOnObstacle();

        System.out.println("All Ocean Explorer tests passed.");
    }

    private static void shouldTurnLeftFromNorthToWest() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(1, 1), Direction.NORTH, "L");

        assertEquals(Direction.WEST, result.finalDirection(), "Turning left from NORTH should face WEST.");
        assertEquals(new Position(1, 1), result.finalPosition(), "Turning should not change position.");
    }

    private static void shouldTurnRightFromNorthToEast() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(1, 1), Direction.NORTH, "R");

        assertEquals(Direction.EAST, result.finalDirection(), "Turning right from NORTH should face EAST.");
        assertEquals(new Position(1, 1), result.finalPosition(), "Turning should not change position.");
    }

    private static void shouldMoveForwardWhenFacingNorth() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(1, 1), Direction.NORTH, "F");

        assertEquals(new Position(1, 2), result.finalPosition(), "Forward move when facing NORTH should increase Y.");
        assertEquals(ExecutionStatus.SUCCESS, result.status(), "Single valid move should succeed.");
    }

    private static void shouldMoveBackwardWhenFacingNorth() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(1, 1), Direction.NORTH, "B");

        assertEquals(new Position(1, 0), result.finalPosition(), "Backward move when facing NORTH should decrease Y.");
    }

    private static void shouldExecuteMultipleCommandsInSequence() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(0, 0), Direction.NORTH, "FFRFF");

        assertEquals(new Position(2, 2), result.finalPosition(), "Sequence FFRFF should end at (2,2).");
        assertEquals(Direction.EAST, result.finalDirection(), "Sequence FFRFF should end facing EAST.");
        assertEquals(
                List.of(
                        new Position(0, 0),
                        new Position(0, 1),
                        new Position(0, 2),
                        new Position(1, 2),
                        new Position(2, 2)),
                result.visitedPositions(),
                "Visited path should contain only successful coordinate changes in order.");
    }

    private static void shouldStopAtBoundary() {
        OceanExplorerController controller = controller(2, 2, Set.of());

        ExecutionResult result = controller.execute(new Position(0, 2), Direction.NORTH, "F");

        assertEquals(ExecutionStatus.BLOCKED_BY_BOUNDARY, result.status(), "Move beyond grid should be boundary-blocked.");
        assertEquals(new Position(0, 2), result.finalPosition(), "Blocked move should leave probe at the last valid position.");
        assertEquals(new Position(0, 3), result.blockedPosition().orElseThrow(), "Blocked position should be reported.");
        assertEquals(List.of(new Position(0, 2)), result.visitedPositions(), "Blocked move should not be added to visited path.");
    }

    private static void shouldStopAtObstacle() {
        OceanExplorerController controller = controller(5, 5, Set.of(new Position(0, 2)));

        ExecutionResult result = controller.execute(new Position(0, 0), Direction.NORTH, "FFR");

        assertEquals(ExecutionStatus.BLOCKED_BY_OBSTACLE, result.status(), "Move into an obstacle should be obstacle-blocked.");
        assertEquals(new Position(0, 1), result.finalPosition(), "Probe should stop at the square before the obstacle.");
        assertEquals(Direction.NORTH, result.finalDirection(), "Later commands should not run after obstacle collision.");
        assertEquals(new Position(0, 2), result.blockedPosition().orElseThrow(), "Obstacle position should be reported.");
    }

    private static void shouldIncludeStartingPositionInVisitedPath() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(3, 3), Direction.SOUTH, "");

        assertEquals(List.of(new Position(3, 3)), result.visitedPositions(), "Empty command string should still record the starting position.");
        assertEquals("(3,3)", result.visitedSummary(), "Visited summary should print coordinates cleanly.");
    }

    private static void shouldKeepOnlyStartingPositionForTurnOnlyCommands() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        ExecutionResult result = controller.execute(new Position(2, 2), Direction.NORTH, "LLRR");

        assertEquals(List.of(new Position(2, 2)), result.visitedPositions(), "Turning should not add duplicate visited coordinates.");
        assertEquals(Direction.NORTH, result.finalDirection(), "Two left turns and two right turns should restore original direction.");
    }

    private static void shouldRejectInvalidCommand() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.execute(new Position(0, 0), Direction.NORTH, "FX"),
                "Invalid commands should fail fast.");
    }

    private static void shouldRejectStartingPositionOutsideGrid() {
        OceanExplorerController controller = controller(5, 5, Set.of());

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.execute(new Position(6, 1), Direction.NORTH, "F"),
                "Starting outside the grid should be rejected.");
    }

    private static void shouldRejectStartingPositionOnObstacle() {
        OceanExplorerController controller = controller(5, 5, Set.of(new Position(1, 1)));

        assertThrows(
                IllegalArgumentException.class,
                () -> controller.execute(new Position(1, 1), Direction.NORTH, "F"),
                "Starting on an obstacle should be rejected.");
    }

    private static OceanExplorerController controller(int maxX, int maxY, Set<Position> obstacles) {
        return new OceanExplorerController(new Grid(maxX, maxY), obstacles);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", but was: " + actual);
        }
    }

    private static void assertThrows(Class<? extends Throwable> expectedType, ThrowingRunnable action, String message) {
        try {
            action.run();
            throw new AssertionError(message + " Expected exception: " + expectedType.getSimpleName());
        } catch (Throwable thrown) {
            if (!expectedType.isInstance(thrown)) {
                throw new AssertionError(
                        message + " Expected exception: " + expectedType.getSimpleName() + ", but was: " + thrown,
                        thrown);
            }
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Throwable;
    }
}

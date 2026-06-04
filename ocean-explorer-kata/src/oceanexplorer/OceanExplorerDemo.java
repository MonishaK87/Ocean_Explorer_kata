package oceanexplorer;

import java.util.Set;

/**
 * Small demo entry point.
 */
public final class OceanExplorerDemo {
    private OceanExplorerDemo() {
    }

    public static void main(String[] args) {
        Grid grid = new Grid(5, 5);
        Set<Position> obstacles = Set.of(new Position(2, 2), new Position(3, 3));

        OceanExplorerController controller = new OceanExplorerController(grid, obstacles);

        ExecutionResult result = controller.execute(new Position(0, 0), Direction.NORTH, "FFRFFLBF");

        System.out.println("Final position : " + result.finalPosition());
        System.out.println("Final direction: " + result.finalDirection());
        System.out.println("Status         : " + result.status());
        System.out.println("Visited path   : " + result.visitedSummary());
        System.out.println("Message        : " + result.message());
    }
}

# Ocean Explorer Kata - Java Solution

## Assumptions
- The grid is bounded and does not wrap.
- Coordinates are zero-based and inclusive from `(0,0)` to `(maxX,maxY)`.
- Valid commands are `F`, `B`, `L`, and `R`.
- Execution stops at the first obstacle or boundary block.
- The starting coordinate is included in the visited-coordinate summary.
- Only successful moves are added to the visited path.
- Invalid command input fails fast with an `IllegalArgumentException`.

## Design
- `Position` is an immutable value object.
- `Direction` owns turning and movement-vector behavior.
- `Grid` is responsible for bounds checking.
- `Probe` owns mutable runtime state and visited path tracking.
- `OceanExplorerController` validates input and executes the command stream.
- `ExecutionResult` reports final state, visited path, and block reason.

## Linux

## How to compile (Linux)
```bash
javac -d out $(find src test -name "*.java")
```

## How to run the tests
```bash
java -cp out oceanexplorer.OceanExplorerTests
```

## How to run the demo
```bash
java -cp out oceanexplorer.OceanExplorerDemo
```


## Windows

## Assumptions

* The grid is bounded (no wrapping behavior).
* Coordinates are zero-based, ranging from `(0,0)` to `(maxX, maxY)` inclusive.
* Supported commands are `F` (forward), `B` (backward), `L` (left), and `R` (right).
* Execution stops immediately when a boundary or obstacle is encountered.
* The starting position is included in the visited path.
* Only successful movements are recorded in the visited path.
* Invalid commands result in a fail-fast behavior using `IllegalArgumentException`.

## Design Approach

The solution is designed using object-oriented principles with clear separation of concerns:

* `Position`: Immutable value object representing coordinates.
* `Direction`: Enum encapsulating turning logic and movement behavior.
* `Grid`: Responsible for validating whether a position is within bounds.
* `Probe`: Maintains the current state (position, direction) and tracks visited positions.
* `OceanExplorerController`: Orchestrates command execution, validates inputs, and enforces movement rules.
* `ExecutionResult`: Encapsulates the final output including position, direction, visited path, and execution status.

## How to Compile (Windows)

### Option 1: Using Command Prompt (CMD)
Open Command Prompt from the project root folder and run:
```bat
mkdir out 
for /R src %f in (*.java) do @echo %f >> sources.txt 
for /R test %f in (*.java) do @echo %f >> sources.txt 
javac -d out @sources.txt
```

### Option 2: Using PowerShell

```powershell
$files = Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
```

## How to Run Tests

```bat
java -cp out oceanexplorer.OceanExplorerTests
```
## Testing

A lightweight dependency-free test suite is included covering:

- Direction changes
- Forward and backward movement
- Command sequences
- Obstacle handling
- Boundary handling
- Invalid commands
- Invalid starting positions
- Path tracking

## How to Run Demo

```bat
java -cp out oceanexplorer.OceanExplorerDemo
```

## Sample output:

Final position : (1,2)  
Final direction: EAST  
Status         : BLOCKED_BY_OBSTACLE  
Visited path   : (0,0) -> (0,1) -> (0,2) -> (1,2)  
Message        : Movement blocked by obstacle at (2,2).  

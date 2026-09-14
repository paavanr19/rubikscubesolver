# Rubik's Cube Solver

A Java-based Rubik's Cube solver that uses **Kociemba's Two-Phase Algorithm** and **IDA*** search.

The cube is represented using its 20 movable pieces: **8 corners and 12 edges**. The solver uses pruning tables to create heuristics for both phases of the search, allowing it to solve the cube without searching the entire state space.

## How It Works

### Cube Representation

The cube is represented using its **cubies** rather than individual stickers.

There are:

* **8 corner pieces**
* **12 edge pieces**
* **6 fixed center pieces**

Each corner stores:

* Its current position
* Its orientation, from `0` to `2`

Each edge stores:

* Its current position
* Its orientation, from `0` to `1`

The corner positions are:

```text
URF, UFL, ULB, UBR,
DFR, DLF, DBL, DRB
```

The edge positions are:

```text
UR, UL, UF, UB,
FR, FL, BL, BR,
DR, DF, DL, DB
```

For example, a corner is represented by which corner piece is currently occupying a particular position and its orientation. This gives the solver a compact representation of the cube state that is well suited for searching permutations and orientations.

---

## Cube Moves

The solver supports all 18 possible face turns:

```text
U   U'  U2
D   D'  D2
R   R'  R2
L   L'  L2
F   F'  F2
B   B'  B2
```

The clockwise moves update the positions and orientations of the affected cubies.

Counter-clockwise and double moves are generated from the clockwise moves:

```text
X' = X X X
X2 = X X
```

This allows all 18 moves to be represented using the same underlying move operations.

---

# Kociemba's Two-Phase Algorithm

The solver uses Kociemba's Two-Phase Algorithm to divide the problem into two smaller search problems.

```text
Scrambled Cube
      │
      ▼
   Phase 1
      │
      ▼
  Restricted
    State
      │
      ▼
   Phase 2
      │
      ▼
 Solved Cube
```

## Phase 1

Phase 1 uses all 18 possible moves.

Its goal is to reach a state where:

* All corner orientations are `0`
* All edge orientations are `0`
* The four middle-layer edges (`FR`, `FL`, `BR`, `BL`) are in the middle layer

Once these conditions are satisfied, the cube is in the subgroup required for Phase 2.

### Phase 1 Pruning Tables

The file `Phase1PruningTable.java` generates three pruning tables.

Each table stores the number of moves required to reach a particular state from the solved state.

The three coordinates are:

1. **Edge orientation**
2. **Corner orientation**
3. **Middle-layer edge placement**

The distances are stored in `HashMap`s and are used as heuristics during the IDA* search.

The Phase 1 heuristic uses the largest of the three distances:

```text
h(n) = max(edgeOrientation,
           cornerOrientation,
           middleEdgePlacement)
```

This gives a lower bound on the number of moves still required because a solution must satisfy all three conditions.

### Phase 1 IDA*

Phase 1 uses **Iterative Deepening A***.

IDA* evaluates each state using:

```text
f(n) = g(n) + h(n)
```

where:

* `g(n)` is the number of moves already made
* `h(n)` is the heuristic distance from the pruning tables

The search starts with an initial threshold and searches all states whose `f(n)` is within that threshold. If no solution is found, the threshold is increased and the search continues.

---

# Phase 2

Once Phase 1 reaches the required subgroup, Phase 2 solves the remaining permutations.

Phase 2 uses only:

```text
U   U'  U2
D   D'  D2
F2
B2
L2
R2
```

This restricted move set preserves the orientation conditions established during Phase 1.

Phase 2 therefore focuses on solving the remaining **corner and edge permutations**.

### Phase 2 Pruning Tables

Phase 2 uses pruning tables for:

* Corner permutation
* Middle-layer edge permutation
* Edge permutation

These tables provide the heuristic values used by the Phase 2 IDA* search.

The Phase 2 heuristic is calculated using the maximum distance from these coordinates:

```text
h(n) = max(cornerPermutation,
           middleEdgePermutation,
           edgePermutation)
```

The search then uses the same IDA* process as Phase 1, but with the smaller Phase 2 move set.

---

# Solving Process

The complete solving process is:

1. Represent the scrambled cube using its corner and edge cubies.
2. Generate a Phase 1 heuristic from the pruning tables.
3. Use IDA* to find a sequence of moves that puts the cube into the required subgroup.
4. Use the Phase 2 pruning tables to generate a new heuristic.
5. Use IDA* with the restricted Phase 2 move set to solve the remaining permutations.
6. Combine the Phase 1 and Phase 2 moves to produce the final solution.

The result is a sequence of standard Rubik's Cube notation moves that solves the original scramble.



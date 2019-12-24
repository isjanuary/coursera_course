import java.util.Comparator;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
  private int moves;
  private boolean isSolvable;
  private final Stack<Board> solution;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) throw new IllegalArgumentException("initial board CANNOT be null");

    // step 1: initialize the Solver
    // construct the initial node and twin node
    SearchNode initNode = new SearchNode(initial, null, 0, false);
    final Comparator<SearchNode> BY_PRIORITY = new PriorityOrder();
    final MinPQ<SearchNode> minManhQ = new MinPQ<SearchNode>(BY_PRIORITY);
    minManhQ.insert(initNode);
    Board twin = initial.twin();
    SearchNode initTwinNode = new SearchNode(twin, null, 0, true);
    minManhQ.insert(initTwinNode);

    SearchNode currNode = null;
    Board currBoard = null;
    int insertNum = 0;
    // step 2: build the A* searching model
    while (!minManhQ.isEmpty()) {
      currNode = minManhQ.delMin();
      currBoard = currNode.board;
      if (currBoard.isGoal()) {
        break;
      }

      for (Board b: currBoard.neighbors()) {
        if (currNode.previous != null && b.equals(currNode.previous.board)) {
          continue;
        }
        insertNum++;
        minManhQ.insert(new SearchNode(b, currNode, currNode.moves + 1, currNode.isTwin));
      }
    }

    System.out.printf("insertNum is: %d\n", insertNum);
    if (currNode.isTwin) {
      this.isSolvable = false;
      this.moves = -1;
      this.solution = null;
    } else {
      this.isSolvable = true;
      this.moves = currNode.moves;
      // build the solution Array
      this.solution = new Stack<Board>();
      while (currNode != null) {
        this.solution.push(currNode.board);
        currNode = currNode.previous;
      }
    }

    // step 3: start A* searching
    // terminal condition:   minManhQ.isEmpty() || someNeighbor.equals(goal)
    // solution 1
    // Board currBoard = null;
    // SearchNode currNode = null;
    // while1:
    // while(!minManhQ.isEmpty()) {
    //   // step3.1: dequeue the min search node
    //   currNode = minManhQ.delMin();
    //   currBoard = currNode.board;
    //   Board previous = currNode.previous;
    //   // step3.2: check if currBoard equals goal board
    //   if (currBoard.equals(goal)) {
    //     break while1;
    //   }

    //   // step3.3: traverse its neighbors, construct search nodes and insert into minManhQ
    //   Iterable<Board> neighbors = currBoard.neighbors();
    //   for (Board nb: neighbors) {
    //     if (nb.equals(goal)) break while1;
    //     if (previous != null && nb.equals(previous)) continue;

    //     SearchNode neighborNode = new SearchNode(nb, currBoard, currNode.moves + 1);
    //     minManhQ.insert(neighborNode);
    //   }
    //   // break;
    //   // step3.4: clear memory
    //   currBoard = null;
    //   currNode = null;
    //   previous = null;
    // }

    // solution 2:  slow solution because if the target board in the next neighbors,
    //              you cannot prove that it will be 100% popped as the result of delMin.
    //              e.g.   we have a minManQ with 2 searchNodes which have priority with 12:
    //              let's say mah1 = 8, moves1 = 4; mah2 = 9, moves = 3.
    //              Now it's move 11, and the target node is among the current searchNode's
    //              neighbors, so the target node priority is move 12, plus manhattan 0.
    //              after we insert this node into the minManQ, we cannot prove that next
    //              delMin operation will pop this target node out.
    // test case:   int[][] testTiles = new int[][]{{2, 7, 0}, {1, 8, 4}, {3, 6, 5}};
    // currNode = initNode;
    // currBoard = initNode.board;
    // while(currBoard.equals(goal) == false || minManhQ.isEmpty()) {
    //   currNode = minManhQ.delMin();
    //   currBoard = currNode.board;
    //   previous = currNode.previous;

    //   Iterable<Board> neighbors = currBoard.neighbors();
    //   for (Board b: neighbors) {
    //     if (previous != null && b.equals(previous)) continue;
    //     SearchNode neighborNode = new SearchNode(b, previous, currNode.moves + 1);
    //     minManhQ.insert(neighborNode);
    //   }
    // }
  }

  /**
   * construct SearchNode data type
   */
  private class SearchNode {
    private final int priority;
    private final int moves;
    private final Board board;
    private final SearchNode previous;
    private final boolean isTwin;

    public SearchNode(Board b, SearchNode previous, int moves, boolean isTwin) {
      this.board = b;
      this.priority = b.manhattan() + moves;
      this.previous = previous;
      this.isTwin = isTwin;
      this.moves = moves;
    }
  }
  private static class PriorityOrder implements Comparator<SearchNode> {
    public int compare(SearchNode s1, SearchNode s2) {
      return s1.priority - s2.priority;
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return this.isSolvable;
  }

  // min number of moves to solve initial board
  public int moves() {
    return this.moves;
  }

  // sequence of boards in a shortest solution
  public Iterable<Board> solution() {
    return this.solution;
  }

  // test client (see below) 
  public static void main(String[] args) {
    // case 1:  solvable
    // int[][] testTiles = {{3, 2, 7}, {0, 1, 5}, {8, 4, 6}};
    // case 2:  solvable
    // int[][] testTiles = {{2, 3, 5}, {4, 7, 6}, {1, 8, 0}};
    // int[][] testTiles = {{3, 4, 2}, {0, 6, 7}, {5, 1, 8}};
    // int[][] testTiles = {{2, 7, 0}, {1, 8, 4}, {3, 6, 5}};
    // case 8: exchange 2 and 5 in case 7
    // int[][] testTiles = {{6, 5, 7}, {0, 1, 2}, {8, 4, 3}};

    // case 3:  simple solvable case
    // int[][] testTiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    // int[][] testTiles = {{1, 2, 3}, {4, 5, 6}, {7, 0, 8}};

    // case 4:  unsolvable case
    // int[][] testTiles = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
    // int[][] testTiles = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 15, 14, 0}};
    // case 5:  exchange 7 and 6 in unsolvable case
    // int[][] testTiles = {{1, 2, 3}, {4, 5, 7}, {8, 6, 0}};

    // 4 * 4 的随机序列
    // [6, 2, 3, 1, 10, 4, 8, 12, 0, 13, 5, 14, 11, 9, 15, 7]
    // int[][] testTiles = {{6, 1, 2, 3}, {4, 10, 8, 14}, {0, 13, 5, 7}, {11, 9, 15, 12}};
    // puzzle04.txt
    // int[][] testTiles = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
    // puzzle3x3-unsolvable.txt
    int[][] testTiles = {{1, 2, 3}, {4, 5, 6}, {8, 7, 0}};
    // int[][] testTiles = {{7, 5, 2}, {8, 0, 1}, {4, 3, 6}};

    Board testBoard = new Board(testTiles);
    Solver solver = new Solver(testBoard);

    boolean isCaseSolvable = solver.isSolvable();
    if (isCaseSolvable) {
      System.out.println("true case");
      System.out.println(solver.moves());
      for (Board b: solver.solution()) {
        System.out.println(b.toString());
      }
    } else {
      System.out.println("false case");
    }
  }
}
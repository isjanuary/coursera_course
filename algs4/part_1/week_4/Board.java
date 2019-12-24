import edu.princeton.cs.algs4.Stack;

public class Board {
  private final int dim;
  private final int[][] tiles;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null || tiles.length < 2)
      throw new IllegalArgumentException("tiles CANNOT be null");
    dim = tiles.length;
    this.tiles = new int[dim][dim];
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        this.tiles[i][j] = tiles[i][j];
      }
    }
  }
              
  // string representation of this board
  public String toString() {
    StringBuilder sbr = new StringBuilder();
    sbr.append(dim);
    for (int i = 0; i < dim; i++) {
      sbr.append("\n");
      for (int j = 0; j < dim; j++) {
        sbr.append((i * 3 + j) % 3 == 0 ? ' ' : "  ");
        sbr.append(this.tiles[i][j]);
      }
    }
    return sbr.toString();
  }

  // board dimension n
  public int dimension() {
    return dim;
  }

  // number of tiles out of place
  public int hamming() {
    int count = 0;
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        count += (tiles[i][j] != 0 && tiles[i][j] != i * dim + j + 1) ? 1 : 0;
      }
    }
    return count;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    int dist = 0;
    int currTile = 0;
    int goalXCord = 0;
    int goalYCord = 0;
    for (int x = 0; x < dim; x++) {
      for (int y = 0; y < dim; y++) {
        if (this.tiles[x][y] == 0) continue;
        currTile = this.tiles[x][y];
        goalYCord = (currTile - 1) % dim;
        goalXCord = (currTile - 1) / dim;
        dist += Math.abs(x - goalXCord);
        dist += Math.abs(y - goalYCord);
      }
    }
    return dist;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return this.hamming() == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (y == null) return false;
    if (y == this) return true;
    if (y.getClass() != this.getClass()) return false;

    Board by = (Board) y;
    if (by.dimension() != this.dim) return false;
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        if (by.tiles[i][j] != this.tiles[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    int zeroXCord = 0;
    int zeroYCord = 0;
    Stack<Board> neighbors = new Stack<Board>();
    // 定位到 0 点坐标
    for (int y = 0; y < dim; y++) {
      for (int x = 0; x < dim; x++) {
        if (this.tiles[y][x] == 0) {
          zeroYCord = y;
          zeroXCord = x;
          break;
        }
      }
    }

    // 根据 0 点坐标生成可能的相邻坐标队列
    // 交换左边和 0 点
    if (zeroXCord > 0) {
      neighbors = swap(zeroXCord, zeroYCord, zeroXCord - 1, zeroYCord, neighbors);
    }
    // 交换右边和 0 点
    if (zeroXCord < dim - 1) {
      neighbors = swap(zeroXCord, zeroYCord, zeroXCord + 1, zeroYCord, neighbors);
      // int[] rightPair = {zeroYCord, zeroXCord + 1};
      // changePositionsQ.enqueue(rightPair);
    }
    // 交换上边和 0 点
    if (zeroYCord > 0) {
      neighbors = swap(zeroXCord, zeroYCord, zeroXCord, zeroYCord - 1, neighbors);
      // int[] topPair = {zeroYCord - 1, zeroXCord};
      // changePositionsQ.enqueue(topPair);
    }
    // 交换下边和 0 点
    if (zeroYCord < dim - 1) {
      neighbors = swap(zeroXCord, zeroYCord, zeroXCord, zeroYCord + 1, neighbors);
      // int[] bottomPair = {zeroYCord + 1, zeroXCord};
      // changePositionsQ.enqueue(bottomPair);
    }
    return neighbors;
  }

  private Stack<Board> swap(int zeroX, int zeroY, int x0, int y0, Stack<Board> neighbors) {
    this.tiles[zeroY][zeroX] = this.tiles[y0][x0];
    this.tiles[y0][x0] = 0;
    neighbors.push(new Board(this.tiles));
    this.tiles[y0][x0] = this.tiles[zeroY][zeroX];
    this.tiles[zeroY][zeroX] = 0;
    return neighbors;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    // swap the first two non-zero elements
    int[][] twinTiles = new int[dim][dim];
    int tmp = 0;
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        twinTiles[i][j] = this.tiles[i][j];
      }
    }

    if (twinTiles[0][0] == 0) {
      tmp = twinTiles[0][1];
      twinTiles[0][1] = twinTiles[1][0];
      twinTiles[1][0] = tmp;
    } else if (twinTiles[0][1] == 0) {
      tmp = twinTiles[0][0];
      twinTiles[0][0] = twinTiles[1][0];
      twinTiles[1][0] = tmp;
    } else {
      tmp = twinTiles[0][1];
      twinTiles[0][1] = twinTiles[0][0];
      twinTiles[0][0] = tmp;
    }

    return new Board(twinTiles);
  }

  // unit testing (not graded)
  public static void main(String[] args) {
    // int[][] tiles = new int[3][3];
    // int[][] targetTiles = new int[3][3];
    // int[] intarr = new int[9];
    // for (int i = 0; i < 9; i++) {
    //   intarr[i] = i;
    // }

    // // StdRandom.shuffle(intarr);
    // for (int i = 0; i < 3; i++) {
    //   for (int j = 0; j < 3; j++) {
    //     tiles[i][j] = intarr[i * 3 + j];
    //     targetTiles[i][j] = i * 3 + j + 1;
    //   }
    // }

    // targetTiles[2][2] = 0;

    // Board board = new Board(tiles);
    // System.out.println(board.toString());
    // System.out.printf("hamming distance is %d\n", board.hamming());
    // System.out.printf("manhattan distance is %d\n", board.manhattan());

    // Iterable<Board> neighbors = board.neighbors();
    // for (Board b: neighbors) {
    //   System.out.println(b.toString());
    // }
    // System.out.println(board.equals(board));
  }
}
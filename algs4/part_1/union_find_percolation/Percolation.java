import edu.princeton.cs.algs4.WeightedQuickUnionUF;

// import java.util.Arrays;
// import edu.princeton.cs.algs4.StdRandom;

/**
 * Percolation
 */
public class Percolation {
  private final boolean[] sites;
  private final WeightedQuickUnionUF uf;
  private final WeightedQuickUnionUF upwardUF;
  private final int dim;
  private int openedCnt;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) throw new IllegalArgumentException("n should be no less than 0");

    dim = n;
    sites = new boolean[n * n + 2];
    uf = new WeightedQuickUnionUF(n * n + 2);
    upwardUF = new WeightedQuickUnionUF(n * n + 1);
    openedCnt = 0;

    // sites[0] and sites[n * n + 1] are virtual sites
    // init all sites blocked by default
    for (int i = 0; i < n * n + 2; i++) {
      sites[i] = false;
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    validate(row, col);

    if (isOpen(row, col)) return;
    // + 1 at last to avoid sites[0], which is the virtual site
    int indice = (row - 1) * dim + col - 1 + 1;
    sites[indice] = true;
    // union row - 1, col    row + 1, col
    // corresponding indice - 1, indice     indice + 1, indice
    // union row, col - 1    row, col + 1
    // corresponding indice, indice - dim    indice, indice + dim
    // connect to top virtual point if first row
    // connect to bottom virtual point if last row
    if (row > 1) {
      if (isOpen(row - 1, col)) {
        uf.union(indice, indice - dim);
        upwardUF.union(indice, indice - dim);
      }
    } else {
      uf.union(indice, 0);
      upwardUF.union(indice, 0);
    }
    if (row < dim) {
      if (isOpen(row + 1, col)) {
        uf.union(indice, indice + dim);
        upwardUF.union(indice, indice + dim);
      }
    } else {
      uf.union(indice, dim * dim + 1);
    }
    if (col > 1 && isOpen(row, col - 1)) {
      uf.union(indice - 1, indice);
      upwardUF.union(indice - 1, indice);
    }
    if (col < dim && isOpen(row, col + 1)) {
      uf.union(indice + 1, indice);
      upwardUF.union(indice + 1, indice);
    }
    openedCnt++;
  }

  // is the site (row, col) open?
  // + 1 at last to avoid sites[0], which is the virtual site
  public boolean isOpen(int row, int col) {
    validate(row, col);
    return sites[(row - 1) * dim + col - 1 + 1];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    validate(row, col);

    if (!isOpen(row, col)) return false;
    int indice = (row - 1) * dim + col - 1 + 1;
    // return uf.connected(indice, 0);
    return upwardUF.connected(indice, 0);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openedCnt;
  }

  // does the system percolate?
  public boolean percolates() {
    return uf.connected(0, dim * dim + 1);
  }

  private void validate(int row, int col) {
    if (row <= 0 || col <= 0 || row > dim || col > dim) {
      throw new IllegalArgumentException("invalid arguments");
    }
  }


  // test client (optional)
  public static void main(String[] args) {
    // for (int t = 0; t < 100; t++) {
    //   Percolation percolation = new Percolation(200);
    //   while (!percolation.percolates()) {
    //     int rowId = StdRandom.uniform(200) + 1;
    //     int colId = StdRandom.uniform(200) + 1;
    //     if (!percolation.isOpen(rowId, colId)) {
    //       percolation.open(rowId, colId);
    //     }
    //   }
    //   int openSites = percolation.numberOfOpenSites();
    //   double threshold = 0.0;
    //   threshold = openSites / 40000.0;
    //   System.out.printf("end: %d %f\n", openSites, threshold);
    // }

    /**
     * backwash test
     */
    // Percolation p = new Percolation(4);
    // p.open(4, 1);
    // p.open(3, 1);
    // p.open(2, 1);
    // p.open(1, 1);
    // p.open(1, 4);
    // p.open(2, 4);
    // p.open(4, 4);
    // System.out.println(p.isFull(4, 4));
    // System.out.println(Arrays.toString(p.sites));
    // System.out.println(Arrays.toString(p.uf.getParent()));
  }
}
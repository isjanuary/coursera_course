import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
// import edu.princeton.cs.algs4.StdIn;;

public class PercolationStats {
  private static final double PERCENT95 = 1.96;
  private double mean = -1.0;
  private double stddev = -1.0;
  private final double[] thresholdRes;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("invalid input arguments");
    }

    thresholdRes = new double[trials];
    for (int t = 0; t < trials; t++) {
      Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        int rowId = StdRandom.uniform(n) + 1;
        int colId = StdRandom.uniform(n) + 1;
        if (!percolation.isOpen(rowId, colId)) {
          percolation.open(rowId, colId);
        }
      }
      thresholdRes[t] = percolation.numberOfOpenSites() / (double) (n * n);
    }
  }

  // sample mean of percolation threshold
  public double mean() {
    mean = StdStats.mean(thresholdRes);
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    stddev = StdStats.stddev(thresholdRes);
    return stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    if (mean < 0) mean = mean();
    if (stddev < 0) stddev = stddev();
    return mean - PERCENT95 * stddev / Math.sqrt(thresholdRes.length);
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    if (mean < 0) mean = mean();
    if (stddev < 0) stddev = stddev();
    return mean + PERCENT95 * stddev / Math.sqrt(thresholdRes.length);
  }

  // test client (see below)
  public static void main(String[] args) {
    // int n = Integer.parseInt(args[0]);
    // int trials = Integer.parseInt(args[1]);
    // PercolationStats ps = new PercolationStats(n, trials);
    PercolationStats ps = new PercolationStats(200, 50);
    System.out.println(ps.mean());
    // System.out.println(ps.stddev());
    System.out.println(ps.confidenceLo());
  }
}

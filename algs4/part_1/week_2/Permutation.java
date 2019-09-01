import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Permutation
 */
public class Permutation {

  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    int cnt = 0;
    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    while (!StdIn.isEmpty()) {
      String str = StdIn.readString();
      rq.enqueue(str);
    }

    for (String s: rq) {
      if (cnt == k) {
        break;
      }
      StdOut.println(s);
      cnt++;
    }
  }
}
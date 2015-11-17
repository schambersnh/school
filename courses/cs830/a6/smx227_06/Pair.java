/**
 * Pair- Simply holds a pair of two objects
 * @author Stephen
 *
 * @param <L>
 * @param <R>
 */
public class Pair<L,R> {

  private final L left;
  private final R right;

  public Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Get the left side of this pair
   * @return
   */
  public L getLeft() { 
	  return left; 
  }
  
  /**
   * Get the right side of this pair
   * @return
   */
  public R getRight() { 
	  return right; 
  }
}

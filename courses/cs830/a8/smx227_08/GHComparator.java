/**
 * Comparator comparing nodes through f(n) = g(n) + h(n)
 */
import java.util.Comparator;


public class GHComparator implements Comparator<Node> {
	@Override
	public int compare(Node o1, Node o2) {
		return Double.compare(o1.gscore + o1.hscore, o2.gscore + o2.hscore);
	}
}

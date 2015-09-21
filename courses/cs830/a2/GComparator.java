/**
 * Comparator comparing nodes through gscores
 */
import java.util.Comparator;


public class GComparator implements Comparator<Node> {
	@Override
	public int compare(Node o1, Node o2) {
		return Integer.compare(o1.gscore, o2.gscore);
	}
}

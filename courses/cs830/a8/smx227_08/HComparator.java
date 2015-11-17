/**
 * Comparator comparing nodes through hscores
 */
import java.util.Comparator;


public class HComparator implements Comparator<Node> {
	@Override
	public int compare(Node o1, Node o2) {
		return Double.compare(o1.hscore, o2.hscore);
	}
}

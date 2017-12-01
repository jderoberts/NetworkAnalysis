package networkZ;

/**
 * The Node class represents a single point in the network with
 * with a unique name and a degree property (number of interactions).
 * Equality is determined by node name
 *
 * @author James Roberts
 * @since 2017-12-01
 *
 */

public class Node {
	public String nodeName;
	public int nodeDegree;
	
	public Node() {
		this.nodeName = "";
		this.nodeDegree = 0;
	}
	
	public Node(String nN) {
		this.nodeName = nN;
		this.nodeDegree = 0;
	}
	
	//copy constructor required for deep copying networks
	public Node(Node another) {
		this.nodeName = another.nodeName;
		this.nodeDegree = another.nodeDegree;
	}
	
	public String toString() {
		return this.nodeName;
	}
	
	public void incrementDegree() {
		this.nodeDegree++;
	}
	
	public void decrementDegree() {
		this.nodeDegree--;
	}
	
	//equality between Node objects determined by shared nodeName
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (! (o instanceof Node)) return false;
		Node other = (Node) o;
		if ((this.nodeName.equals(other.nodeName))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//hashcode based on nodeName to prevent duplication in HashSets
	@Override
	public int hashCode() {
		return (int) this.nodeName.hashCode();
	}
}

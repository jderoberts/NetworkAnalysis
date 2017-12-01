package networkZ;

/**
 * The Edge class represents the interaction between a pair of Node objects.
 * Equality is determined by its members; edges are undirected interactions 
 * so Edge[A,B] == Edge[B,A]
 *  
 * @author James Roberts
 * @since 2017-12-01
 *
 */

public class Edge {
	public Node[] nodes = new Node[2];
	
	public Edge(Node n1, Node n2) {
		this.nodes[0] = n1;
		this.nodes[1] = n2;
	}
	
	//copy constructor required for deep copying networks 
	public Edge(Edge another) {
		this.nodes[0] = new Node(another.nodes[0]);
		this.nodes[1] = new Node(another.nodes[1]);
	}
	
	public String toString() {
		return this.nodes[0]+"-"+this.nodes[1];
	}
	
	//equality between Edge objects determined by shared Nodes
	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		if (! (o instanceof Edge)) return false;
		Edge other = (Edge) o;
		if ((this.nodes[0].equals(other.nodes[0])&&this.nodes[1].equals(other.nodes[1])) || (this.nodes[0].equals(other.nodes[1])&&this.nodes[1].equals(other.nodes[0]))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//hashcode based on both Node members to prevent duplication in HashSets
	@Override
	public int hashCode() {
		return (int) this.nodes[0].hashCode()*this.nodes[1].hashCode();
	}
}

package networkZ;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * The Network class represents a collection of nodes and the interactions
 * between those nodes (edges).
 * Includes constructors for reading from file or duplicating existing networks
 * 
 * @author James Roberts
 * @since 2017-12-01
 *
 */

public class Network {
	public HashSet<Node> nodeList;
	public HashSet<Edge> edgeList;
	
	public Network() {
		//basic constructor - empty network
		this.nodeList = new HashSet<Node>();
		this.edgeList = new HashSet<Edge>();
	}
		
	public Network(Network another) { //EXTENSION
		//copy constructor, allowing complete duplication of network objects
		this.nodeList = new HashSet<Node>();
		this.edgeList = new HashSet<Edge>();
		for (Node node : another.nodeList) {
			this.nodeList.add(new Node(node));
		};
		for (Edge edge : another.edgeList) {
			this.edgeList.add(new Edge(edge));
		};
	}
	
	public Network (ArrayList<Node> nodes, Network parent) { //EXTENSION
		//subnetwork constructor, saving a subset of nodes as a separate network
		this.nodeList = new HashSet<Node>();
		this.edgeList = new HashSet<Edge>();
		for (Node node : nodes) {
			this.nodeList.add(new Node(node.nodeName));
		};
		//for each edge in the parent network, add to child if contains a relevant node
		for (Edge edge : parent.edgeList) {
			if (this.nodeList.contains(edge.nodes[0])||this.nodeList.contains(edge.nodes[1])) {
				this.addInteraction(edge.nodes[0], edge.nodes[1]);
			}
		};
	}
	
	public Network(String infN) {
		//import constructor - from filename
		this.nodeList = new HashSet<Node>();
		this.edgeList = new HashSet<Edge>();
		this.readFile(infN);
	}
	
	public void readFile(String infN) {
		//helper function for import constructor
		//take input file, open.
		//split into prot1 and prot2
		//for each pair of values in list, call create edge function 
		Path infile = Paths.get(infN);
		try(BufferedReader reader = Files.newBufferedReader(infile)) {
			String line = null;
			while((line = reader.readLine()) != null) {
				//skip blank lines
				if (line.equals("")) {continue;};
				String[] elements = line.split("\t");
				//if only one element is present raise error and move on
				try {
					this.addInteraction(new Node(elements[0]),new Node(elements[1]));
				}
				catch (ArrayIndexOutOfBoundsException x) {
					System.err.format("Error: two elements were not found at line <%s>\n",line);
				}
			}
			reader.close();
		}
		catch (IOException x){
			System.err.format("IO exception: %s %n", x);
		}
	}
	

	public Node getNode(Node nN) {
		//helper function for node lookups
		//find requested node and return object
		Node nx = null;
		for (Node counter : this.nodeList) {
			if (counter.equals(nN)) {
				nx = counter;
			}
		}
		return nx;
	}

	public int getDegree(String nN) { 
		//find requested node and look up degree member
		int degree = 0;
		Node nx = new Node (nN);
		try {
			degree = getNode(nx).nodeDegree;
		} catch (Exception e) {
			//return 0 if not found
		}
		return degree;
	};
	
	public ArrayList<Node> getPartners(Node nN) { 	//EXTENSION
		//return a list of all nodes that interact with specified node
		ArrayList<Node> nList = new ArrayList<Node>();
		for (Edge counter : this.edgeList) {
			if (counter.nodes[0].equals(nN)) {
				nList.add(counter.nodes[1]);
			}
			//avoids including self-interactions twice
			if (counter.nodes[1].equals(nN) && !counter.nodes[0].equals(nN)) {
				nList.add(counter.nodes[0]);
			}
		}
		return nList;
	};
	

	public String nodeSummary(String nN) {	//EXTENSION
		//formatted output of getDegree and getPartners - for use by GUI
		//find requested node and look up degree member
		Node nx = new Node (nN);
		int degree = getDegree(nN);
		StringBuilder summary = new StringBuilder();
		summary.append("\nNode summary: "+nN);
		if (degree == 0) {
			summary.append("\n\nNode not found in network.");
			return summary.toString();
		}
		summary.append("\nDegree: "+degree);
		summary.append("\n\nInteractions:\n");
		for (Node counter : this.getPartners(nx)) {
			summary.append("\n"+counter.toString());
		}
		return summary.toString();
	}
	

	public void addInteraction(Node n1, Node n2) { 
		//adding to existing network object
		//add each node to nodeList (or do nothing if present)
		this.nodeList.add(n1);
		this.nodeList.add(n2);
		//add new edge to edgeList (or do nothing if exists)
		//if successful, add 1 to degree of n1 and n2 (if not, do nothing)
		if (this.edgeList.add(new Edge(n1,n2))) {
			this.getNode(n1).incrementDegree();
			this.getNode(n2).incrementDegree(); //loops increase degree by 2
			
		};
	};
	
	public ArrayList<Node> removeInteraction(Node n1, Node n2) { 	//EXTENSION
		//removing from existing network object
		//remove specified edge from edgeList (or do nothing if doesn't exist)
		//if successful, remove 1 from degree of n1 and n2 (if not, do nothing)
		if (this.edgeList.remove(new Edge(n1,n2))) {
			this.getNode(n1).decrementDegree();
			this.getNode(n2).decrementDegree(); //loops decrease degree by 2
		};
		//remove nodes if no longer connected to network and return names
		ArrayList<Node> removedList = new ArrayList<Node>();
		try {
			if (this.getNode(n1).nodeDegree == 0) {
				removedList.add(new Node(n1));
				this.nodeList.remove(n1);
			}
			//skip if n1 == n2 (removing a loop)
			if (!(n1.equals(n2))&&(this.getNode(n2).nodeDegree == 0)) {
				removedList.add(new Node(n2));
				this.nodeList.remove(n2);
			}
		} catch (NullPointerException nex) {
			//if nodes do not exist nx.nodeDegree will result in exception - return empty list
		}
		return removedList;
	};
	
	public ArrayList<Node> removeNode(String nN) {	//EXTENSION
		//remove specified node from existing network and all associated interactions
		Node nx = new Node(nN);
		//build up list of edges to remove to avoid ConcurrentModificationException
		List<Edge> toRemove = new ArrayList<>();
		//build up list of removed nodes to return
		ArrayList<Node> removedList = new ArrayList<Node>();
		for (Edge counter : this.edgeList) {
			if (counter.nodes[0].equals(nx) || counter.nodes[1].equals(nx)) {
				toRemove.add(counter);
			}
		}
		for (Edge counter : toRemove) {
			//add any returned nodes
			removedList.addAll(this.removeInteraction(counter.nodes[0],counter.nodes[1]));
		}
		return removedList;
	}
	
	public double getAverageDegree() {
		//return average degree of all nodes in network
		double totalDegree = 0;
		for (Node counter : this.nodeList) {
			totalDegree += counter.nodeDegree;
		}
		return totalDegree/this.nodeList.size();
	};
	
	public ArrayList<Node> getNodesByDegree(int degree) { 	//EXTENSION
		//return a list of all nodes with the specified degree
		ArrayList<Node> nList = new ArrayList<Node>();
		for (Node counter : this.nodeList) {
			if (counter.nodeDegree == degree) {
				nList.add(counter);
			}
		}
		return nList;
	};
	
	public Map<Integer,Integer> degreeDistribution() { 
		//returns HashMap of degree distribution for use by GUI or to save to file
		Map<Integer,Integer> degreeDistr = new TreeMap<Integer,Integer>(Collections.reverseOrder());
		for (Node counter : this.nodeList) {
			if (!degreeDistr.containsKey(counter.nodeDegree)) {
				degreeDistr.put(counter.nodeDegree, 1);
			}
			else {
				int prev = degreeDistr.get(counter.nodeDegree);
				degreeDistr.put(counter.nodeDegree, prev + 1);
			}
		}
		return degreeDistr;
	};
	
	public void saveDegrees(String outfN) {
		//save degree distr as text file
		Map<Integer,Integer> degreeDistr = this.degreeDistribution();
		Path outfile = Paths.get(outfN); 
		try(BufferedWriter writer = Files.newBufferedWriter(outfile)) {
			writer.write("Degree\tNodes\n");
			for (Map.Entry<Integer,Integer> degree : degreeDistr.entrySet()) {
	            writer.write(degree.getKey() + "\t" + degree.getValue() + "\n");
			}
			writer.close();
		}
		catch (IOException x){
			System.err.format("IO exception: %s %n", x);
		}
	};
	
	public String networkSummary() {	//EXTENSION
		//summary of network characteristics for use by GUI
		StringBuilder summary = new StringBuilder();
		summary.append(" summary:\n");
		summary.append("\nNodes: "+ this.nodeList.size());
		summary.append("\nEdges: "+ this.edgeList.size());
		summary.append("\n\nDegree Distribution:");
		summary.append("\nDegree:\tNodes:");
		Map<Integer,Integer> degreeDistr = this.degreeDistribution();
		for (Map.Entry<Integer,Integer> degree : degreeDistr.entrySet()) {
			summary.append(String.format("\n%8s\t%10s", degree.getKey(), degree.getValue()));
		}
		summary.append(String.format("\nAverage Degree: %.2f",this.getAverageDegree()));
		return summary.toString();
	}

	public ArrayList<Node> getHubs() { 
		//return a list of nodes with the highest degree
		ArrayList<Node> hubList = new ArrayList<Node>();
		try {
			Map<Integer,Integer> degreeDistr = this.degreeDistribution();
			Map.Entry<Integer,Integer> hubs = degreeDistr.entrySet().iterator().next();
			int hubDegree = hubs.getKey();
			hubList = this.getNodesByDegree(hubDegree);
		} catch (NoSuchElementException ex) {
			//do nothing - empty map from blank network
		}
		return hubList;
	};
	
	public String writeInteractions() {	//EXTENSION
		//returns string of network characteristics for use by GUI or to save as file
		StringBuilder interactions = new StringBuilder();
		for (Edge counter : this.edgeList) {
            interactions.append(counter.nodes[0] + "\t" + counter.nodes[1] + "\n");
		}
		return interactions.toString();
	}
	
	public void exportNetwork(String outfN) {	//EXTENSION
		//save network to .txt file (including any added interactions)
		Path outfile = Paths.get(outfN); 
		try(BufferedWriter writer = Files.newBufferedWriter(outfile)) {
            writer.write(this.writeInteractions());
			writer.close();
		}
		catch (IOException x){
			System.err.format("IO exception: %s %n", x);
		}
	}
	
	public ArrayList<ArrayList<Node>> subNetworks() {	//EXTENSION
		//returns a list of separate groups of interconnected nodes
		//Create deep copies of all nodes to allow modification
		HashSet<Node> remainingNodes = new HashSet<Node>();
		for (Node node : this.nodeList) {
			remainingNodes.add(new Node(node));
		};
		ArrayList<ArrayList<Node>> subNetworks = new ArrayList<ArrayList<Node>>();
		while (remainingNodes.size() > 0) { 
			ArrayList<Node> checkedNodes = new ArrayList<Node>();
			ArrayList<Node> workingNodes = new ArrayList<Node>();
			//assign the first node in the set to nx and add to working set
			Node nx = null;
			for (Node counter : remainingNodes) {
				nx = counter;
				workingNodes.add(nx);
				remainingNodes.remove(nx);
				break;
			}
			//build up working set of interacting partners until all nodes have been checked
			while (workingNodes.size() > 0) {
				Node nz = workingNodes.get(0); 
				for (Node counter : this.getPartners(nz)) {
					if (!checkedNodes.contains(counter) && !workingNodes.contains(counter) ) {
						workingNodes.add(counter);
					}
				}
				checkedNodes.add(nz);
				remainingNodes.remove(nz);
				workingNodes.remove(nz);
			}
			subNetworks.add(checkedNodes);
			//repeat while there are nodes not accounted for
		}
		return subNetworks;
	}
}


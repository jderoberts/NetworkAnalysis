# NetworkAnalysis
Java programming project - Bioinformatics MSc, University of Manchester

Representing biological protein-interaction networks through Node, Edge and Network classes, with GUI.

Input: import from tab-delimited .txt file of protein interactions or create blank network

Output: tab-delimited .txt network file/degree distribution of network
  
Help documentation included in GUI:
  
Creating Networks
--------------------
Networks consist of undirected interactions between nodes.  Node names are case-sensitive; Node1 and node1 would be added to the network separately.

Self interactions can be included in the network.  The resulting loops increase the node's degree by two.

Exporting Networks
--------------------
A summary of the network degree distribution can be saved as a tab-delimited .txt file.

The network can also be exported as a tab-delimited .txt file of pairs of interactions, allowing networks to be saved and reloaded later.

Modifying Networks
--------------------
Networks can be modified by adding or removing interactions between nodes.  Single nodes can also be deleted along with all of their interactions, e.g. to simulate gene knockout or protein inhibition.

Modifying networks may result in disconnected networks, in which all nodes do not form a continuous network.  The selected network can be checked for sub-networks and all detected components saved as new networks. The contents of the sub-networks can be checked by exporting them as .txt files or by viewing them in the GUI window.

Single nodes that are no longer connected to any other nodes (with degree 0) are removed from the network automatically.

Comparing Networks
--------------------
Networks can be copied.  Any changes made to the resulting copy will not affect the original, allowing quick comparison of the effects of any changes on the network characteristics.

A network summary is displayed when it is selected from the list of open networks.

  

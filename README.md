# NetworkAnalysis
This is a Java programming project completed as part of my Bioinformatics MSc at the University of Manchester.
The application represents biological protein-interaction networks through Node, Edge and Network classes with a GUI. 

### Input:
 - Network of binary interactions
 - Import from tab-delimited .txt file of protein interactions or create blank network

### Output
 - Tab-delimited .txt network file
 - Degree distribution of network

![Application menus gif](/docs/networkz_main.gif)
  
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

![Subnetworks gif](/docs/networkz_split.gif)

Comparing Networks
--------------------
Networks can be copied.  Any changes made to the resulting copy will not affect the original, allowing quick comparison of the effects of any changes on the network characteristics. 

A network summary is displayed when it is selected from the list of open networks. 


## Learning outcomes:
 - Java GUI with AWT and Swing
 - Error handling and dealing with unexpected input
 - Deep and shallow copying
 - Equality in custom classes: overriding equals and hashcode

The assignment specifications required 3 data structure classes (Node, Edge and Network) and a GUI class.
The resulting classes are large with numerous methods, especially the GUI class: they could benefit from refactoring to split into smaller classes handling the different behaviours.  

![Class diagram](/docs/networkz_uml.png) 

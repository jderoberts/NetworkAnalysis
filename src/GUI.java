package networkZ;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import javax.swing.UIManager;
import javax.swing.border.*;

import javax.swing.ListSelectionModel;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.util.ArrayList;

/**
 * The GUI class sets up the GUI and controls the application logic,
 * using the data structures defined in Network, Edge and Node.
 *
 * @author James Roberts
 * @since 2017-12-01
 *
 */

public class GUI {

	private JFrame frame;
	int currentNetwork = 0;
	ArrayList<Network> networks = new ArrayList<Network>();
	ArrayList<String> networkNames = new ArrayList<String>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			//if unsuccessful, do nothing and use default Metal appearance 
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		frame = new JFrame("NetworkZ Analysis");
		frame.setSize(new Dimension(490, 300));
		frame.setMaximumSize(new Dimension(490, 2147483647));
		frame.setPreferredSize(new Dimension(490, 300));
		frame.setMinimumSize(new Dimension(490, 300));
		frame.setBounds(100, 100, 490, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			frame.setIconImage(Toolkit.getDefaultToolkit().getImage("networkZ_icon.png"));
		} catch (Exception ex) {
			//if not found, use default java icon
		}
		
		//main frame components - created before menu items so they can be referred to in event handlers
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane.setViewportBorder(null);
		JTextPane txtPane = new JTextPane();
		scrollPane.setViewportView(txtPane);
		txtPane.setBorder(new EmptyBorder(0, 20, 0, 10));
		txtPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		separator.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblMessage = new JLabel(" ");
		lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblMessage.setForeground(new Color(0, 100, 0));
		lblMessage.setPreferredSize(new Dimension(450, 15));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Networks:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		DefaultListModel<String> listContents = new DefaultListModel<String>();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setViewportBorder(null);
		scrollPane_1.setPreferredSize(new Dimension(124, 173));
		panel.add(scrollPane_1);
		JList<String> list = new JList<String>();
		scrollPane_1.setViewportView(list);
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(list.getValueIsAdjusting()) {
					return;
				}
				try {
					currentNetwork = list.getSelectedIndex();
					txtPane.setText("\n"+networkNames.get(currentNetwork)+networks.get(currentNetwork).networkSummary());
					txtPane.setCaretPosition(1);
				} catch (Exception ex) {
					//if not changing to a valid index, do nothing
					txtPane.setText(ex.toString());
				}
			}
		});
		list.setModel(listContents);
		list.setSize(new Dimension(125, 175));
		list.setBorder(null);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		networks.add(new Network());
		networkNames.add("Network1");
		listContents.addElement("Network1");
		
		//introductory prompt
		txtPane.setText("\r\nCreate a network from a tab-delimited .txt file of interactions to begin, or manually add interactions to a blank network.\n\nAn empty network has been started automatically.");
		
		//menu bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//Network dropdown menu
		JMenu mnNetwork = new JMenu("Network");
		menuBar.add(mnNetwork);
		//Network/new from file
		JMenuItem mntmNewNetwork = new JMenuItem("Create new network from file");
		mntmNewNetwork.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmNewNetwork.setPreferredSize(new Dimension(180,22)); //reverses jmenuitem height shrinking to item height
		mnNetwork.add(mntmNewNetwork);
		mntmNewNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	final JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            JTextField networkName = new JTextField();
		            networkName.setText(file.getName().replaceAll(".txt",""));
		            Object[] message = {
						"Network name: ",networkName
					};
					int option = JOptionPane.showConfirmDialog(null, message, "New network from file", JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
					    if (networkName.getText().isEmpty()) {
					    	lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Network name was left blank.  Please provide a network name.");
					    } else if (networkNames.contains(networkName.getText())) {
					    	lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Network name is already in use.  Please provide a unique name.");
						} else {
							lblMessage.setForeground(new Color(0, 100, 0));
							lblMessage.setText("Network created: "+networkName.getText());
					    	networks.add(new Network(file.getAbsolutePath()));
							networkNames.add(networkName.getText());
							listContents.addElement(networkName.getText());
					    }
					}
				}
		    }
		});
		//network/new blank
		JMenuItem mntmBlankNetwork = new JMenuItem("Create new blank network");
		mntmBlankNetwork.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmBlankNetwork.setPreferredSize(new Dimension(180,22));
		mnNetwork.add(mntmBlankNetwork);
		mntmBlankNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField networkName = new JTextField();
				Object[] message = {
					"Network name: ",networkName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "New blank network", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (networkName.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name was left blank.  Please provide a network name.");
				    } else if (networkNames.contains(networkName.getText())) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name is already in use.  Please provide a unique name.");
					} else {
						lblMessage.setForeground(new Color(0, 100, 0));
						lblMessage.setText("Network created: "+networkName.getText());
						networks.add(new Network());
						networkNames.add(networkName.getText());
						listContents.addElement(networkName.getText());
				    }
				}
		    }
		});
		//network/separator
		mnNetwork.addSeparator();
		//network/export degrees
				JMenuItem mntmExportDegreeDistribution = new JMenuItem("Export degree distribution");
				mntmExportDegreeDistribution.setMinimumSize(new Dimension(0, 22));
				mntmExportDegreeDistribution.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
				mntmExportDegreeDistribution.setPreferredSize(new Dimension(180,22));
				mnNetwork.add(mntmExportDegreeDistribution);
				mntmExportDegreeDistribution.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent ae) {
				    	final JFileChooser fc = new JFileChooser();
						fc.setSelectedFile(new File(networkNames.get(currentNetwork)+"_degrees.txt"));
						int returnVal = fc.showSaveDialog(frame);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
				            File file = fc.getSelectedFile();
				            networks.get(currentNetwork).saveDegrees(file.getAbsolutePath());
				            lblMessage.setForeground(new Color(0, 100, 0));
				            lblMessage.setText("Degree distribution for "+networkNames.get(currentNetwork)+" saved to: " + file.getName());
						}
				    }
				});
		//network/export network
		JMenuItem mntmExportNetwork = new JMenuItem("Export network as file");
		mnNetwork.add(mntmExportNetwork);
		mntmExportNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	final JFileChooser fc = new JFileChooser();
				fc.setSelectedFile(new File(networkNames.get(currentNetwork)+".txt"));
				int returnVal = fc.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            networks.get(currentNetwork).exportNetwork(file.getAbsolutePath());
		            lblMessage.setForeground(new Color(0, 100, 0));
		            lblMessage.setText(networkNames.get(currentNetwork)+" saved to: " + file.getName());
				}
		    }
		});
		//network/export network
				JMenuItem mntmDisplayNetwork = new JMenuItem("Display network interactions");
				mnNetwork.add(mntmDisplayNetwork);
				mntmDisplayNetwork.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent ae) {
				    	String interactions = networks.get(currentNetwork).writeInteractions();
				    	if (interactions.equals("")) {
							lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Empty network selected.");
						} else {
					    	txtPane.setText(String.format("\nInteractions in network %s:\n\n%s",networkNames.get(currentNetwork),interactions));
							lblMessage.setText("");
						}
				    }
				});
		//network/separator2
		mnNetwork.addSeparator();
		//network/rename
		JMenuItem mntmRenameNetwork = new JMenuItem("Rename current network");
		mnNetwork.add(mntmRenameNetwork);
		mntmRenameNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField networkName = new JTextField();
	            networkName.setText(networkNames.get(currentNetwork));
	            String oldName = networkNames.get(currentNetwork);
	            Object[] message = {
					"Network name: ", networkName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Rename network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (networkName.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name was left blank.  Please provide a network name.");
				    } else if (networkNames.contains(networkName.getText())) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name is already in use.  Please provide a unique name.");
					} else {
						lblMessage.setForeground(new Color(0, 100, 0));
						lblMessage.setText("Network renamed from "+ oldName + " to "+networkName.getText());
				    	networkNames.set(currentNetwork,networkName.getText());
						listContents.setElementAt(networkName.getText(), currentNetwork);
						//list.setSelectedIndex(currentNetwork);  do I need this?
				    }
				}
		    }
		});
		//network/duplicate
		JMenuItem mntmDuplicateNetwork = new JMenuItem("Duplicate current network");
		mnNetwork.add(mntmDuplicateNetwork);
		mntmDuplicateNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField networkName = new JTextField();
	            networkName.setText(networkNames.get(currentNetwork)+"_copy");
	            Object[] message = {
					"Network name: ",networkName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Copy of network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (networkName.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name was left blank.  Please provide a network name.");
				    } else if (networkNames.contains(networkName.getText())) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Network name is already in use.  Please provide a unique name.");
					} else {
						lblMessage.setForeground(new Color(0, 100, 0));
						lblMessage.setText("Duplicate network created: "+networkName.getText());
						networks.add(new Network(networks.get(currentNetwork)));
						networkNames.add(networkName.getText());
						listContents.addElement(networkName.getText());
				    }
				}
		    }
		});
		//network/delete
		JMenuItem mntmDeleteNetwork = new JMenuItem("Delete current network");
		mnNetwork.add(mntmDeleteNetwork);
		mntmDeleteNetwork.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
				Object[] message = {
					"Deleted networks cannot be recovered.\nWould you like to proceed?"
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Delete network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					lblMessage.setForeground(new Color(0, 100, 0));
					lblMessage.setText("Deleted network: "+networkNames.get(currentNetwork));
					String removedName = networkNames.get(currentNetwork);
					networks.remove(networks.get(currentNetwork));
					listContents.removeElement(removedName);
					networkNames.remove(removedName);
					//if no remaining networks, create blank network
			    	if (networkNames.size()==0) {
			    		networks.add(new Network());
			    		networkNames.add("Network1");
			    		listContents.addElement("Network1");
			    		lblMessage.setText(lblMessage.getText()+". Blank network Network1 created.");
			    	}
			    	currentNetwork = 0;
			    	list.setSelectedIndex(0);
				}
		    }
		});
		
		//edit menu
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		//edit/add
		JMenuItem mntmAddNewInteraction = new JMenuItem("Add new interaction");
		mntmAddNewInteraction.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmAddNewInteraction.setPreferredSize(new Dimension(180,22));
		mnEdit.add(mntmAddNewInteraction);
		mntmAddNewInteraction.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField node1 = new JTextField();
				JTextField node2 = new JTextField();
				Object[] message = {
				    "Node #1:", node1,
				    "Node #2:", node2
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Add interaction to: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (node1.getText().isEmpty() || node2.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Insufficient arguments: two nodes needed.");
				    } else {
				    	networks.get(currentNetwork).addInteraction(new Node(node1.getText()), new Node(node2.getText()));
				    	lblMessage.setForeground(new Color(0, 100, 0));
				    	lblMessage.setText("Interaction added to "+networkNames.get(currentNetwork)+": "+node1.getText()+" to "+node2.getText());
				    }
				}
		    }
		});
		//edit/remove
		JMenuItem mntmRemoveExistingInteraction = new JMenuItem("Remove existing interaction");
		mnEdit.add(mntmRemoveExistingInteraction);
		mntmRemoveExistingInteraction.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField node1 = new JTextField();
				JTextField node2 = new JTextField();
				Object[] message = {
					"Node #1:", node1,
				    "Node #2:", node2
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Remove interaction from: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (node1.getText().isEmpty() || node2.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Insufficient arguments: two nodes needed.");
				    } else {
				    	ArrayList<Node> removedNodes = networks.get(currentNetwork).removeInteraction(new Node(node1.getText()), new Node(node2.getText()));
				    	if (removedNodes.size()>0) {
				    		txtPane.setText("\nThe following nodes were disconnected from the rest of the network and have been removed:\n");
				    		for (Node node : removedNodes) {
				    			txtPane.setText(txtPane.getText()+"\n"+node);
				    		}
				    	}
				    	lblMessage.setForeground(new Color(0, 100, 0));
				    	lblMessage.setText("Interaction removed from "+networkNames.get(currentNetwork)+": "+node1.getText()+" to "+node2.getText());
				    }
				}
		    }
		});
		//edit/remove node
		JMenuItem mntmRemoveNode = new JMenuItem("Remove node");
		mnEdit.add(mntmRemoveNode);
		mntmRemoveNode.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField nodeName = new JTextField();
				Object[] message = {
					"The selected node will be removed from the network.\nAny resulting nodes with degree 0 will also be removed.\nWould you like to proceed?",
					"Node name:", nodeName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Remove node from: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (nodeName.getText().isEmpty()) {
						lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Node name was left blank.  Please provide a node name.");
				    } else {
				    	ArrayList<Node> removedNodes = networks.get(currentNetwork).removeNode(nodeName.getText());
				    	if (removedNodes.size()>0) {
				    		txtPane.setText("\nThe following nodes were disconnected from the rest of the network and have been removed:\n");
				    		for (Node node : removedNodes) {
				    			txtPane.setText(txtPane.getText()+"\n"+node);
				    		}
				    		lblMessage.setText("");
				    	} else {
				    		lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Node "+nodeName.getText()+ " was not found in "+networkNames.get(currentNetwork));
				    	}
				    }
				}
		    }
		});
		
		//search menu
		JMenu mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);
		//search/lookup
		JMenuItem mntmLookupNodeDegree = new JMenuItem("Look up node degree");
		mntmLookupNodeDegree.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmLookupNodeDegree.setPreferredSize(new Dimension(180,22));
		mnSearch.add(mntmLookupNodeDegree);
		mntmLookupNodeDegree.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField nodeName = new JTextField();
				Object[] message = {
					"Node name: ",nodeName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Look up node in network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (nodeName.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Node name was left blank.  Please provide a node name.");
				    } else {
				    	int nodeD = networks.get(currentNetwork).getDegree(nodeName.getText());
					    if (nodeD != 0) {
					    	txtPane.setText("\nNode: "+nodeName.getText()+"\n\nDegree: "+nodeD);
					    	lblMessage.setText("");
					    } else {
					    	lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Node: "+nodeName.getText()+" not found in network "+networkNames.get(currentNetwork));
				    	}
				    }
				}
		    }
		});
		//search/hubs
		JMenuItem mntmGetNetworkHubs = new JMenuItem("Get network hubs");
		mntmGetNetworkHubs.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmGetNetworkHubs.setPreferredSize(new Dimension(180,22));
		mnSearch.add(mntmGetNetworkHubs);
		mntmGetNetworkHubs.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	ArrayList<Node> networkHubs = networks.get(currentNetwork).getHubs();
				if (networkHubs.size() > 0) {
					txtPane.setText("\nNetwork hubs:\n");
					String hubNode = "";
					for (Node counter : networkHubs) {
						txtPane.setText(txtPane.getText()+"\n"+counter.nodeName);
						hubNode = counter.nodeName;
					}
					int nodeD = networks.get(currentNetwork).getDegree(hubNode);
					txtPane.setText(txtPane.getText()+"\n\nHub degree: "+nodeD);
					lblMessage.setText("");
				} else {
					lblMessage.setForeground(new Color(255, 0, 0));
			    	lblMessage.setText("Empty network selected.");
				}
		    }
		});
		//search/average
		JMenuItem mntmGetAverageDegree = new JMenuItem("Get average degree");
		mntmGetAverageDegree.setIcon(new ImageIcon(GUI.class.getResource("/javax/swing/plaf/metal/icons/sortDown.png")));
		mntmGetAverageDegree.setPreferredSize(new Dimension(180,22));
		mnSearch.add(mntmGetAverageDegree);
		mntmGetAverageDegree.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	double averageDegree = networks.get(currentNetwork).getAverageDegree();
				if (Double.isNaN(averageDegree)) {
					lblMessage.setForeground(new Color(255, 0, 0));
			    	lblMessage.setText("Empty network selected.");
				} else {
			    	txtPane.setText(String.format("\nAverage Degree: %.2f",averageDegree));
					lblMessage.setText("");
				}
		    }
		});
		//search/separator
		mnSearch.addSeparator();
		//search/degree search
		JMenuItem mntmNodesByDegree = new JMenuItem("Find nodes by degree");
		mnSearch.add(mntmNodesByDegree);
		mntmNodesByDegree.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField nodeDegree = new JTextField();
				Object[] message = {
					"Degree: ", nodeDegree
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Look up nodes in network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (nodeDegree.getText().isEmpty() || ! nodeDegree.getText().matches("^[0-9]*$")) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Invalid degree given.  Please provide an integer degree.");
				    } else {
				    	ArrayList<Node> degreeNodes = networks.get(currentNetwork).getNodesByDegree(Integer.parseInt(nodeDegree.getText()));
				    	txtPane.setText("\nNodes with degree "+nodeDegree.getText()+":\n");
						for (Node counter : degreeNodes) {
							txtPane.setText(txtPane.getText()+"\n"+counter.nodeName);
						}
						lblMessage.setForeground(new Color(0, 100, 0));
				    	lblMessage.setText("");
				    }
				}
		    }
		});
		//search/node summary
		JMenuItem mntmNodeSummary = new JMenuItem("Node summary");
		mnSearch.add(mntmNodeSummary);
		mntmNodeSummary.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	JTextField nodeName = new JTextField();
				Object[] message = {
					"Node name: ",nodeName
				};
				int option = JOptionPane.showConfirmDialog(null, message, "Node summary from network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
				    if (nodeName.getText().isEmpty()) {
				    	lblMessage.setForeground(new Color(255, 0, 0));
				    	lblMessage.setText("Node name was left blank.  Please provide a node name.");
				    } else {
				    	txtPane.setText(networks.get(currentNetwork).nodeSummary(nodeName.getText()));
				    	lblMessage.setText("");
				    }
				}
		    }
		});
		//search/subnetworks
		JMenuItem mntmSubNetworks = new JMenuItem("Find sub-networks");
		mnSearch.add(mntmSubNetworks);
		mntmSubNetworks.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	ArrayList<ArrayList<Node>> subNetworks = networks.get(currentNetwork).subNetworks(); 
				if (subNetworks.size() == 0) {
					lblMessage.setForeground(new Color(255, 0, 0));
			    	lblMessage.setText("Empty network selected.");
				} else if (subNetworks.size() == 1) {
					txtPane.setText("\nNo sub-networks found.\nAll nodes form one connected network.");
				} else if (subNetworks.size() > 10) {
					JTextField groupMinSize = new JTextField();
					groupMinSize.setText("2");
					Object[] message = {
							subNetworks.size()+" unconnected sub-networks were found." +
									"\nSave each component as a new network?"+
									"\n\nOptional: Save only groups of N nodes or greater",
									groupMinSize
					};
					int option = JOptionPane.showConfirmDialog(null, message, "Disconnected network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						if (groupMinSize.getText().isEmpty() || ! groupMinSize.getText().matches("^[0-9]*$")) {
					    	lblMessage.setForeground(new Color(255, 0, 0));
					    	lblMessage.setText("Invalid group size given.  Please provide an integer greater than or equal to 2.");
					    	return;
					    }
						//constructor based on list of nodes and parent network - pull out relevant edges
						int j = 0;
						int k = 0;
						int minSize = Integer.parseInt(groupMinSize.getText());
						for (ArrayList<Node> group : subNetworks) {
							if (group.size() < minSize) {
								k++;
								continue;
							}
							j++;
							networks.add(new Network(group,networks.get(currentNetwork)));
							networkNames.add(networkNames.get(currentNetwork)+"_sub"+j);
							listContents.addElement(networkNames.get(currentNetwork)+"_sub"+j);
						}
						if (k > 0 && j > 0) {
							lblMessage.setForeground(new Color(0, 100, 0));
							lblMessage.setText(String.format("%d sub-networks below size threshold, %d sub-networks saved.",k,j));
						} else if (j > 0){
							lblMessage.setForeground(new Color(0, 100, 0));
							lblMessage.setText(String.format("%d subnetworks saved.",j));
						} else {
							lblMessage.setForeground(new Color(255, 0, 0));
							lblMessage.setText("No sub-networks found above size threshold.");
						}
					}
				} else {
					StringBuilder groups = new StringBuilder();
					int i = 0;
					for (ArrayList<Node> group : subNetworks) {
						i++;
						groups.append("\nGroup "+i+": "+group.size()+" nodes");
					}
					Object[] message = {
						"Multiple unconnected sub-networks were found:"+groups.toString()+"\nSave each component as a new network?"
					};
					int option = JOptionPane.showConfirmDialog(null, message, "Disconnected network: "+networkNames.get(currentNetwork), JOptionPane.OK_CANCEL_OPTION);
					if (option == JOptionPane.OK_OPTION) {
						//constructor based on list of nodes and parent network - pull out relevant edges
						int j = 0;
						for (ArrayList<Node> group : subNetworks) {
							j++;
							networks.add(new Network(group,networks.get(currentNetwork)));
							networkNames.add(networkNames.get(currentNetwork)+"_sub"+j);
							listContents.addElement(networkNames.get(currentNetwork)+"_sub"+j);
						}
					}
				lblMessage.setText("");
				}
		    }
		});
		
		//help menu
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnAbout.add(mntmHelp);
		mntmHelp.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ae) {
		    	//included as string due to assignment file upload limitations, would otherwise be imported from .txt as needed
		    	String helpText = "\n" +
		    	"Creating Networks\n--------------------\n" +
		    	"Networks consist of undirected interactions between nodes.  Node names are case-sensitive; Node1 and node1 would be added to the network separately.\n\n" +
		    	"Self interactions can be included in the network.  The resulting loops increase the node's degree by two.\n\n" +
		    	"Exporting Networks\n--------------------\n" +
		    	"A summary of the network degree distribution can be saved as a tab-delimited .txt file.\n\n" +
		    	"The network can also be exported as a tab-delimited .txt file of pairs of interactions, allowing networks to be saved and reloaded later.\n\n" +
		    	"Modifying Networks\n--------------------\n" +
		    	"Networks can be modified by adding or removing interactions between nodes.  Single nodes can also be deleted along with all of their interactions, e.g. to simulate gene knockout or protein inhibition.\n\n" +
		    	"Modifying networks may result in disconnected networks, in which all nodes do not form a continuous network.  The selected network can be checked for sub-networks and all detected components saved as new networks. " +
		    	"The contents of the sub-networks can be checked by exporting them as .txt files or by viewing them in the GUI window.\n\n" +
		    	"Single nodes that are no longer connected to any other nodes (with degree 0) are removed from the network automatically.\n\n" +
		    	"Comparing Networks\n--------------------\n" +
		    	"Networks can be copied.  Any changes made to the resulting copy will not affect the original, allowing quick comparison of the effects of any changes on the network characteristics.\n\n" +
		    	"A network summary is displayed when it is selected from the list of open networks.\n";
		    	txtPane.setText(helpText);
		    	txtPane.setCaretPosition(1);
		    }
		});
		
		//layout handler
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
							.addGap(10)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 450, GroupLayout.PREFERRED_SIZE))
					.addGap(14))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
					.addGap(10)
					.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(10))
		);
				
		frame.getContentPane().setLayout(groupLayout);
	}
}

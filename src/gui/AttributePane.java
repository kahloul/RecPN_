package gui;

import static gui.Style.ATTRIBUTE_PANE_BORDER;
import static gui.Style.ATTRIBUTE_PANE_DIMENSION;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import petrinet.Arc;
import petrinet.INode;
import petrinet.IRenew;
import petrinet.Place;
import petrinet.RenewCount;
import petrinet.RenewId;
import petrinet.RenewMap;
import petrinet.Transition;
import engine.attribute.PlaceAttribute;
import engine.attribute.TransitionAttribute;
import engine.handler.NodeTypeEnum;
import exceptions.EngineException;

/** Singleton class that represents the attribute chart at the middle top */
public class AttributePane {

	/** Singleton instancce */
	private static AttributePane instance;

	// static constructor that initiates the singleton instance and constants
	static {
		instance = new AttributePane();
	}

	/** Returns the only instance of the AttributePane */
	public static AttributePane getInstance() {
		return instance;
	}

	/** Table containing data for current node */
	private JTable table;

	/** Panel for the Attributes */
	private JPanel attributePane;

	/** Private Constructor that configures the pane */
	private AttributePane() {
		table = initiateTable();
		attributePane = initiateAttributePane(table);
	}

	/**
	 * Initialize the Attributepane and set the Dimension and Layout Add the
	 * Table to the Pane
	 */
	private JPanel initiateAttributePane(JTable table) {
		JPanel pane = new JPanel();
		pane.setBorder(ATTRIBUTE_PANE_BORDER);
		pane.setMinimumSize(ATTRIBUTE_PANE_DIMENSION);
		pane.setLayout(new GridLayout(1, 1));

		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(false);

		pane.add(scrollPane);
		return pane;
	}

	/** Initiate the Table for Attributepane */
	private JTable initiateTable() {
		JTable table = new JTable();// content,columnNames);
		return table;
	}

	void displayNode(INode node, PetrinetViewer petrinetViewer) {
		try {
			NodeTypeEnum type = (NodeTypeEnum) EngineAdapter
					.getPetrinetManipulation().getNodeType(node);
			String id = String.valueOf(node.getId());
			AbstractPetriTableModel tableModel = null;
			if (type == NodeTypeEnum.Place) {
				PlaceAttribute placeAttribute = petrinetViewer
						.getPlaceAttribute((Place) node);
				String name = placeAttribute.getPname();
				String mark = String.valueOf(placeAttribute.getMarking());

				tableModel = new PlaceTableModel(id, name, mark);
			} else {
				TransitionAttribute transitionAttribute = petrinetViewer
						.getTransitionAttribute((Transition) node);
				String name = transitionAttribute.getTname();
				String tlb = transitionAttribute.getTLB();
				IRenew renew = transitionAttribute.getRNW();
				String renewString = "unbekannt";
				if (renew instanceof RenewCount) {
					renewString = "count";
				} else if (renew instanceof RenewId) {
					renewString = "id";
				} else if (renew instanceof RenewMap) {
					renewString = "map: " + renew;
				}
				tableModel = new TransitionTableModel(id, name, tlb,
						renewString);
			}
			tableModel.addTableModelListener(new TableListener(petrinetViewer,
					node));
			table.setModel(tableModel);
		} catch (EngineException e) {
			e.printStackTrace();
		}
	}

	void displayEdge(Arc edge, PetrinetViewer petrinetViewer) {
		String weight = String.valueOf(petrinetViewer.getArcAttribute(edge)
				.getWeight());
		String id = String.valueOf(edge.getId());

		ArcTableModel arcTableModel = new ArcTableModel(id, weight);
		TableListener tableListener = new TableListener(petrinetViewer, edge);
		arcTableModel.addTableModelListener(tableListener);

		table.setModel(arcTableModel);
		// table.getModel().addTableModelListener(tableListener);
	}
	

	public void displayEmpty() {
		DefaultTableModel defaultTableModel = new DefaultTableModel();
		table.setModel(defaultTableModel);
	}

	/**
	 * Add this Panel to given frame
	 * 
	 * @param frame
	 *            wich the pane add to
	 */
	public void addTo(JPanel frame) {
		frame.add(attributePane, BorderLayout.CENTER);
	}

	/** Disable the tabe */
	void setTableDisable() {
		table.setEnabled(false);
	}

	/** Enable the tabe */
	void setTableEnable() {
		table.setEnabled(true);
	}

	/**
	 * Abstract super class for refactoring the three Table Models for Place,
	 * Transition and Arc<br/>
	 * Table Model for displaying and editing places. All cells need to be
	 * String<br/>
	 * A TableModel is needed to tell Swing what cells are editable
	 */
	private static abstract class AbstractPetriTableModel extends
			AbstractTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6737819176767454927L;

		protected abstract String[][] getData();

		/** Head of table */
		protected String[] columnNames = { "Atrribut", "Wert" };

		@Override
		public int getRowCount() {
			return getData().length;
		}

		@Override
		public int getColumnCount() {
			return getData()[0].length;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return getData()[rowIndex][columnIndex];
		}

		/**
		 * Set wich rows and columns are editable. the first row and the first
		 * column are not editable
		 */
		@Override
		public boolean isCellEditable(int row, int col) {
			// only bottom right is editable
			if (row > 0 && col > 0) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			getData()[rowIndex][columnIndex] = (String) aValue;
			fireTableCellUpdated(rowIndex, columnIndex);
		}

	}

	/** Class for the Table with Place Attributes */
	private static class PlaceTableModel extends AbstractPetriTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6357182574839407504L;
		/**
		 * The General Data for the Placetable
		 */
		private String[][] data = { { "Id", "" }, { "Name", "" },
				{ "Markierung", "" } };

		/** Initiates the table with actual data for id, name, and mark */
		public PlaceTableModel(String id, String name, String mark) {
			data[0][1] = id;
			data[1][1] = name;
			data[2][1] = mark;
		}

		/** Returns all data (whole table except the head) */
		@Override
		protected String[][] getData() {
			return data;
		}
	}

	/** Class for the Table with Transition Attributes */
	private static class TransitionTableModel extends AbstractPetriTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3955002911201794539L;
		/**
		 * The General Data for the Transitiontable
		 */
		private String[][] data = { { "Id", "" }, { "Name", "" },
				{ "Label", "" }, { "Renew", "" } };

		/** Initiates the table with actual data for id, name, laben and renew */
		public TransitionTableModel(String id, String name, String label,
				String renew) {
			data[0][1] = id;
			data[1][1] = name;
			data[2][1] = label;
			data[3][1] = renew;
		}

		/** Returns all data (whole table except the head) */
		@Override
		protected String[][] getData() {
			return data;
		}
	}

	/** Class for the Table with Arc Attributes */
	private static class ArcTableModel extends AbstractPetriTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6770322595921463169L;
		/**
		 * The General Data for the Arctable
		 */
		private String[][] data = { { "Id", "" }, { "Gewicht", "" }, };

		/** Initiates the table with actual data for id an name */
		public ArcTableModel(String id, String weight) {
			data[0][1] = id;
			data[1][1] = weight;
		}

		/** Returns all data (whole table except the head) */
		@Override
		protected String[][] getData() {
			return data;
		}

	}

	/** Class for Tablelistener to make Userchanges possible */
	private static class TableListener implements TableModelListener {

		private PetrinetViewer petrinetViewer;

		private INode node;

		private Arc arc;

		TableListener(PetrinetViewer petrinetViewer, INode node) {
			this.petrinetViewer = petrinetViewer;
			this.node = node;
			this.arc = null;
		}

		TableListener(PetrinetViewer petrinetViewer, Arc arc) {
			this.petrinetViewer = petrinetViewer;
			this.node = null;
			this.arc = arc;
		}

		@Override
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int column = e.getColumn();
			TableModel model = (TableModel) e.getSource();
			String data = (String) model.getValueAt(row, column);
			String attribute = (String) model.getValueAt(row, column - 1);
			/* ARC */
			if (arc != null) {
				if (attribute.equals("Gewicht")) {
					try {
						petrinetViewer.setWeight(arc, Integer.parseInt(data));
						// MainWindow.getPetrinetManipulation().setWeight(
						// petrinetId, arc, Integer.parseInt(data));
					} catch (NumberFormatException e1) {
						PopUp.popError("Das Gewicht muss eine natürliche Zahl sein.");
						e1.printStackTrace();
					}
				}
			} else if (node != null) {
				/* PLACE */
				if (petrinetViewer.isNodePlace(node)) {
					Place place = (Place) node;
					if (attribute.equals("Name")) {
						petrinetViewer.setPname(place, data);
					} else if (attribute.equals("Markierung")) {
						try {
							int marking = Integer.parseInt(data);
							petrinetViewer.setMarking(place, marking);
						} catch (NumberFormatException nfe) {
							PopUp.popError("Die Markierung muss eine natürliche Zahl sein.");
						}
					}

					/* TRANSITION */
				} else {
					Transition transition = (Transition) node;
					if (attribute.equals("Name")) {
						petrinetViewer.setTname(transition, data);
					} else if (attribute.equals("Label")) {
						petrinetViewer.setTlb(transition, data);
					} else if (attribute.equals("Renew")) {
						// engine needs setRenew
					}
				}
			}
			petrinetViewer.smartRepaint();
		}

	}
}
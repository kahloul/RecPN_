package gui2;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Transformer;

import petrinet.Arc;
import petrinet.INode;
import petrinet.Place;
import petrinet.Transition;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.renderers.Renderer.Vertex;
import edu.uci.ics.jung.visualization.transform.shape.GraphicsDecorator;
import engine.attribute.ArcAttribute;
import engine.attribute.PlaceAttribute;
import engine.attribute.TransitionAttribute;
import engine.handler.NodeTypeEnum;
import exceptions.EngineException;
import gui2.EditorPane.EditorMode;

import static gui2.Style.*;

/** Pane for displaying petrinets */
class PetrinetPane {

	/** Internal JPanel for gui-layouting the petrinet */
	private JPanel petrinetPanel;

	/** Internal Panel for scrolling and zooming. This is added to petrinetPanel */
	private GraphZoomScrollPane scrollPanel;

	/** For zooming */
	private ScalingControl scaler;

	/** Internal JPanel for actually viewing and controlling the petrinet */
	private VisualizationViewer<INode, Arc> visualizationViewer;

	/** ID of currently displayed petrinet */
	int currentPetrinetId = -1;

	/** singleton instance of this pane */
	private static PetrinetPane instance;

	/** Returns the singleton instance for this pane */
	public static PetrinetPane getInstance() {
		return instance;
	}

	/** mouse click listener for the drawing panel */
	private static class PetrinetMouseListener extends
			PickingGraphMousePlugin<INode, Arc> implements MouseWheelListener {

		private static enum DragMode {
			SCROLL, MOVENODE, ARC, NONE
		}

		private PetrinetPane petrinetPane;

		private DragMode dragMode = DragMode.NONE;

		PetrinetMouseListener(PetrinetPane petrinetPane) {
			this.petrinetPane = petrinetPane;
		}

		/** X-coordinate of begin of drag */
		private int pressedX = 0;
		/** Y-coordinate of begin of drag */
		private int pressedY = 0;

		/**
		 * For defining maximim zoom. Places are not displayed correctly if the
		 * user zooms too deep
		 */
		float currentZoom = 1;

		/**
		 * ID of node that was clicked at beginning of drag. Needed for drawing
		 * arcs
		 */
		private INode nodeFromDrag = null;

		/** Zoom petrinet on mouse wheel */
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Point point = new Point(e.getX(), e.getY());
			if (e.getWheelRotation() < 0) {
				if (currentZoom * 1.1f <= 1) {
					petrinetPane.scaler.scale(petrinetPane.visualizationViewer,
							1.1f, point);
					currentZoom *= 1.1f;
				}
			} else {
				petrinetPane.scaler.scale(petrinetPane.visualizationViewer,
						0.9f, point);
				currentZoom *= 0.9f;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			super.mousePressed(e); // mousePressedEvent in class
									// PickingGraphMousePlugin selects nodes
			EditorMode mode = EditorPane.getInstance().getCurrentMode();

			if (mode == EditorMode.PICK) {
				if (edge != null) {
					AttributePane.getInstance().displayEdge(edge);
					if (e.isMetaDown()) {
						PetrinetPopUpMenu.fromArc(edge).show(
								PetrinetPane.getInstance().visualizationViewer,
								e.getX(), e.getY());
					}

				} else if (vertex != null) {
					AttributePane.getInstance().displayNode(vertex);
					if (e.isMetaDown()) {
						PetrinetPopUpMenu.fromNode(vertex).show(
								PetrinetPane.getInstance().visualizationViewer,
								e.getX(), e.getY());
					}
				}
			} else
				try {
					if (mode == EditorMode.PLACE) {
						MainWindow.getPetrinetManipulation().createPlace(
								petrinetPane.currentPetrinetId,
								new Point(e.getX(), e.getY()));
					} else if (mode == EditorMode.TRANSITION) {
						MainWindow.getPetrinetManipulation().createTransition(
								petrinetPane.currentPetrinetId,
								new Point(e.getX(), e.getY()));
					}
					petrinetPane.petrinetPanel.repaint();
				} catch (EngineException e1) {
					e1.printStackTrace();
				}
		}

		/** Checks if something is clicked without changing selection */
		private boolean isAnythingClicked(MouseEvent e) {
			Arc beforeEdge = edge;
			INode beforeNode = vertex;
			edge = null;
			vertex = null;
			super.mousePressed(e);

			boolean result = edge != null || vertex != null;
			edge = beforeEdge;
			vertex = beforeNode;

			return result;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (dragMode == DragMode.NONE) {
				pressedX = e.getX();
				pressedY = e.getY();
				EditorMode editorMode = EditorPane.getInstance()
						.getCurrentMode();
				// dragging in pick mode
				if (editorMode == EditorMode.PICK) {
					// find >out: scrolling or moving? -> is something clicked?
					if (isAnythingClicked(e)) {
						// something is clicked -> MOVENODE
						dragMode = DragMode.MOVENODE;
					} else {
						// nothing is clicked -> SCROLL
						dragMode = DragMode.SCROLL;
					}
					// dragging in arc mode
				} else if (editorMode == EditorMode.ARC) {
					// find out what was clicked on
					super.mousePressed(e);
					if (vertex != null) {
						nodeFromDrag = vertex;
						vertex = null;
						dragMode = DragMode.ARC;
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Point oldPoint = new Point(pressedX, pressedY);
			Point newPoint = new Point(e.getX(), e.getY());

			if (!oldPoint.equals(newPoint)) {
				if (dragMode == DragMode.SCROLL) {
					/*
					 * Scrolling is realized by zooming out of old position and
					 * zooming in to new position
					 */
					petrinetPane.scaler.scale(petrinetPane.visualizationViewer,
							0.5f, newPoint);
					petrinetPane.scaler.scale(petrinetPane.visualizationViewer,
							2f, oldPoint);
				} else if (dragMode == DragMode.MOVENODE) {
					PopUp.popUnderConstruction("Knoten verschieben");
				} else if (dragMode == DragMode.ARC) {
					// find out what was released on
					super.mousePressed(e);
					if (vertex != null) {
						try {
							MainWindow.getPetrinetManipulation().createArc(
									petrinetPane.currentPetrinetId,
									nodeFromDrag, vertex);
						} catch (EngineException e1) {
							e1.printStackTrace();
						}

					}
				}
			}
			dragMode = DragMode.NONE;
		}
	} // end of mouse listener

	private static class PetrinetPopUpMenu extends JPopupMenu {

		private static class MenuListener implements ActionListener {

			private MenuListener() {
			}

			private Transition transition;

			private Place place;

			private Arc arc;

			private int pId;

			private MenuListener(Transition transition, Place place, Arc arc,
					int pId) {
				this.transition = transition;
				this.place = place;
				this.arc = arc;
				this.pId = pId;
			}

			static MenuListener fromPlace(Place place, int pId) {
				return new MenuListener(null, place, null, pId);
			}

			static MenuListener fromArc(Arc arc, int pId) {
				return new MenuListener(null, null, arc, pId);
			}

			static MenuListener fromTransition(Transition transition, int pId) {
				return new MenuListener(transition, null, null, pId);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (place != null) {
						MainWindow.getPetrinetManipulation().deletePlace(pId,
								place);
					} else if (transition != null) {
						MainWindow.getPetrinetManipulation().deleteTransition(
								pId, transition);
					} else {
						MainWindow.getPetrinetManipulation().deleteArc(pId, arc);
					}
				} catch (EngineException e1) {
					PopUp.popUnderConstruction("löschen");
					e1.printStackTrace();
				}
			}
		}

		private PetrinetPopUpMenu() {
		}

		static PetrinetPopUpMenu fromNode(INode node) {
			PetrinetPopUpMenu result = new PetrinetPopUpMenu();
			int pId = PetrinetPane.getInstance().currentPetrinetId;
			try {
				if (MainWindow.getPetrinetManipulation().getNodeType(node) == NodeTypeEnum.Place) {
					PlaceAttribute placeAttribute = MainWindow
							.getPetrinetManipulation().getPlaceAttribute(pId,
									node);
					JMenuItem jMenuItem = new JMenuItem("Stelle "
							+ placeAttribute.getPname() + " [" + node.getId()
							+ "] " + "löschen");
					jMenuItem.addActionListener(MenuListener
							.fromPlace((Place) node, pId));
					result.add(jMenuItem);
				} else {
					TransitionAttribute transitionAttribute = MainWindow
							.getPetrinetManipulation()
							.getTransitionAttribute(
									PetrinetPane.getInstance().currentPetrinetId,
									node);
					JMenuItem jMenuItem = new JMenuItem("Transition "
							+ transitionAttribute.getTname() + " ["
							+ node.getId() + "] " + "löschen");
					jMenuItem.addActionListener(MenuListener
							.fromTransition((Transition) node, pId));
					result.add(jMenuItem);
				}
			} catch (EngineException e) {
				PopUp.popError(e);
				e.printStackTrace();
			}
			return result;
		}

		static PetrinetPopUpMenu fromArc(Arc arc) {
			PetrinetPopUpMenu result = new PetrinetPopUpMenu();
			JMenuItem jMenuItem = new JMenuItem("Pfeil [" + arc.getId()
					+ "] löschen");
			jMenuItem.addActionListener(MenuListener.fromArc(arc,
					PetrinetPane.getInstance().currentPetrinetId));
			result.add(jMenuItem);
			return result;
		}
	}

	/**
	 * Custom renderer that is used from jung to make transitions cornered and
	 * places circular
	 */
	private static class PetrinetRenderer implements Vertex<INode, Arc> {

		@Override
		public void paintVertex(RenderContext<INode, Arc> renderContext,
				Layout<INode, Arc> layout, INode node) {
			GraphicsDecorator decorator = renderContext.getGraphicsContext();
			Point2D center = layout.transform(node);
			try {
				if (MainWindow.getPetrinetManipulation().getNodeType(node) == NodeTypeEnum.Place) {
					// PlaceAttribute placeAttribute =
					// MainWindow.getPetrinetManipulation().getPlaceAttribute(PetrinetPane.getInstance().currentPetrinetId,
					// node);
					// commented because Engine mock throws exception
					int width = 20;
					int height = 15;
					int x = (int) (center.getX() - width / 2);
					int y = (int) (center.getY() - height / 2);
					Color lightBlue = new Color(200, 200, 250);
					decorator.setPaint(lightBlue);
					// decorator.setPaint(placeAttribute.getColor());
					// //commented because Engine mock throws exception
					decorator.fillOval(x, y, width, height);

					decorator.setPaint(FONT_COLOR);
					decorator.drawString(node.getName(), x + width, y + height);
					// decorator.drawString(placeAttribute.getPname(), x +
					// width, y + height);
					// commented because Engine mock throws exception
				} else {
					TransitionAttribute transitionAttribute = MainWindow
							.getPetrinetManipulation()
							.getTransitionAttribute(
									PetrinetPane.getInstance().currentPetrinetId,
									node);

					Color blackOrWhite = null;
					if (transitionAttribute.getIsActivated()) {
						blackOrWhite = Color.BLACK;
					} else {
						blackOrWhite = Color.LIGHT_GRAY;
					}

					int size = 20;
					int x = (int) (center.getX() - size / 2);
					int y = (int) (center.getY() - size / 2);
					decorator.setPaint(blackOrWhite);
					decorator.fillRect(x, y, size, size);

					decorator.setPaint(FONT_COLOR);
					decorator.drawString(transitionAttribute.getTname(), x
							+ size, y + size);
				}
			} catch (EngineException e) {
				PopUp.popError(e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Transforming Arcs to Strings. Actually just used for getting the label of
	 * an Arc
	 */
	private static class PetrinetArcLabelTransformer implements
			Transformer<Arc, String> {
		@Override
		public String transform(Arc arc) {
			int weight = 1;
			try {
				ArcAttribute arcAttribute = MainWindow
						.getPetrinetManipulation().getArcAttribute(
								PetrinetPane.getInstance().currentPetrinetId,
								arc);
				weight = arcAttribute.getWeight();
			} catch (EngineException e) {
				e.printStackTrace();
			}
			if (weight == 1) {
				return "";
			} else {
				return String.valueOf(weight);
			}
		}
	}

	/* Static constructor that initiates the singleton */
	static {
		instance = new PetrinetPane();
	}

	/** Private default constructor */
	private PetrinetPane() {
		petrinetPanel = new JPanel();
		petrinetPanel.setLayout(PETRINET_PANE_LAYOUT);
		petrinetPanel.setBorder(PETRINET_BORDER);

		visualizationViewer = initializeVisualizationViever();
		scaler = new CrossoverScalingControl();
		visualizationViewer.setBackground(Color.WHITE);
		scrollPanel = new GraphZoomScrollPane(visualizationViewer);

		petrinetPanel.add(scrollPanel);

		visualizationViewer.addMouseListener(new PetrinetMouseListener(this));
		visualizationViewer.addMouseWheelListener(new PetrinetMouseListener(
				this));
		visualizationViewer.getRenderer().setVertexRenderer(
				new PetrinetRenderer()); // make transitions and places look
											// like transitions and places

		visualizationViewer.getRenderContext().setEdgeLabelTransformer(
				new PetrinetArcLabelTransformer());
	}

	private JPanel getPetrinetPanel() {
		return petrinetPanel;
	}

	/**
	 * Returns the graphLayout for the currently displayed petrinet
	 * 
	 * @return <tt>null</tt> if Error was thrown
	 */
	private AbstractLayout<INode, Arc> getCurrentLayout() {
		try {
			return MainWindow.getPetrinetManipulation().getJungLayout(
					currentPetrinetId);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return null;
	}

	private VisualizationViewer<INode, Arc> initializeVisualizationViever() {
		currentPetrinetId = MainWindow.getPetrinetManipulation()
				.createPetrinet();

		VisualizationViewer<INode, Arc> visuServer = new VisualizationViewer<INode, Arc>(
				getCurrentLayout());

		return visuServer;
	}

	/** Adds the petrinet pane to the given JPanel (frame) */
	public void addTo(JPanel frame) {
		frame.add(getPetrinetPanel());
	}

	public void repaint() {
		visualizationViewer.repaint();
	}
}

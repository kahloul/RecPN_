package gui2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.sun.org.apache.xml.internal.security.signature.Manifest;

import petrinet.Arc;
import petrinet.INode;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import engine.EngineComponent;
import engine.EngineMockup;
import engine.ihandler.IPetrinetManipulation;
import exceptions.EngineException;
import gui2.EditorPane.EditorMode;

import static gui2.Style.*;

/** Pane for displaying petrinets */
class PetrinetPane {
	
	/** Internal JPanel for gui-layouting the petrinet */
	private JPanel petrinetPanel;
	
	/** Internal Panel for scrolling and zooming. This is added to petrinetPanel */
	private GraphZoomScrollPane scrollPanel;
	
	/** Internal JPanel for actually viewing and controlling the petrinet */
	private VisualizationViewer<INode, Arc> visualizationViewer;
	
	/** ID of currently displayed petrinet */
	int currentPetrinetId = -1;
	
	/** singleton instance of this pane */
	private static PetrinetPane instance;
	
	/** Returns the singleton instance for this pane */
	public static PetrinetPane getInstance(){
		return instance;
	}
	
	/** mouse click listener for the drawing panel */
	private static class PetrinetMouseClickListener extends PickingGraphMousePlugin<INode, Arc>{
		
		private PetrinetPane petrinetPane;
		
		PetrinetMouseClickListener(PetrinetPane petrinetPane){
			this.petrinetPane = petrinetPane;
		}
		
		/** Indicates whether a new drag has begun or not */
		private boolean pressedBefore = false;
		/** X-coordinate of begin of drag */
		private int pressedX = 0;
		/** Y-coordinate of begin of drag */
		private int pressedY = 0;
		@Override public void mouseClicked(MouseEvent e) {
			super.mousePressed(e);
			EditorMode mode = EditorPane.getInstance().getCurrentMode();
			int x = e.getX();
			int y = e.getY();

			if( mode == EditorMode.PICK){
				System.out.println(""+edge+vertex);
				if(edge != null){
					AttributePane.getInstance().displayEdge(edge);
					System.out.println("Kante " + edge + " wurde angeklickt");
				}
				if(vertex != null){
					AttributePane.getInstance().displayNode(vertex);
					System.out.println("Knoten " + vertex + " wurde angeklickt");
				}
			}
			System.out.println("mouse clicked on petrinet at [" + e.getX() + 
					"," + e.getY() +
					"] in mode: " + EditorPane.getInstance().getCurrentMode());
		}
//		@Override public void mouseEntered(MouseEvent e) {}
//		@Override public void mouseExited(MouseEvent e) {}
		@Override public void mousePressed(MouseEvent e) {
			if(EditorPane.getInstance().getCurrentMode() == EditorMode.PICK && !pressedBefore){
				pressedX = e.getX();
				pressedY = e.getY();
				pressedBefore = true;
			}
		}
		@Override public void mouseReleased(MouseEvent e) {
			if(EditorPane.getInstance().getCurrentMode() == EditorMode.PICK && pressedBefore){
				pressedBefore = false;
				System.out.println("TODO: Translate petrinet by [" + 
						(e.getX() - pressedX) + "," + 
						(e.getY() - pressedY) + "]");
			}
		}
	}
	
	/* Static constructor that initiates the singleton */
	static {
		instance = new PetrinetPane();
	}
	
	/** Private default constructor */
	private PetrinetPane(){
		petrinetPanel = new JPanel();
		petrinetPanel.setLayout(PETRINET_PANE_LAYOUT);
		petrinetPanel.setBorder(PETRINET_BORDER);
		
		visualizationViewer = dirtyTest(); //This is for ad hoc testing the engine
		visualizationViewer.setBackground(Color.WHITE);
		scrollPanel = new GraphZoomScrollPane(visualizationViewer);
		
		petrinetPanel.add(scrollPanel);
		
		visualizationViewer.addMouseListener(new PetrinetMouseClickListener(this));
	}
	
	private JPanel getPetrinetPanel(){
		return petrinetPanel;
	}
	
	/** Returns the graphLayout for the currently displayed petrinet 
	 * @return <tt>null</tt> if Error was thrown*/
	private AbstractLayout<INode, Arc> getCurrentLayout() {
		try {
			return MainWindow.getPetrinetManipulation().getJungLayout(currentPetrinetId);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** For quick testing if displaying works 
	 * don't try this at home*/
	private VisualizationViewer<INode, Arc> dirtyTest(){
		PickingGraphMousePlugin<INode, Arc> mouse = new PickingGraphMousePlugin<INode, Arc>();
		currentPetrinetId = MainWindow.getPetrinetManipulation()
				.createPetrinet();

		VisualizationViewer<INode, Arc> visuServer = new VisualizationViewer<INode, Arc>(
				getCurrentLayout());

		return visuServer;
		
//		engine.createPetrinet();
		
		
		
		
//		IPetrinetManipulation engine = PetrinetManipulation.getInstance();
//		
//		int petrinetId = engine.createPetrinet();
//		BasicVisualizationServer<INode, Arc> visServer = null;
//		try {
//			engine.createPlace(petrinetId, new Point(0,0));
//			AbstractLayout<INode, Arc> layout = engine.getJungLayout(petrinetId);
//			visServer = new BasicVisualizationServer<INode, Arc>(layout);
//		} catch (EngineException e) {
//			e.printStackTrace();
//		}
//		
//		return visServer;
//		
	}
	
	/** Adds the petrinet pane to the given JPanel (frame) */
	public void addTo(JPanel frame){
		frame.add(getPetrinetPanel());
	}
}

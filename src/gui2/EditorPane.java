package gui2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import static gui2.Style.*;

/** The Panel that contains buttons for Editor-Modes like Pick, Translate, AddTransition, AddPlace etc */
class EditorPane {
	
	/** Internal JPanel */
	private JPanel editorPane;
	
	/** The group containing all 4 buttons. Only one can be selected */
	private ButtonGroup buttonGroup;
	
	/** Button for choosing pick mode */
	private JRadioButton pickButton;
	
	/** Button for choosing create place mode */
	private JRadioButton createPlaceButton;
	
	/** Button for choosing create transition mode */
	private JRadioButton createTransitionButton;
	
	/** Button for choosing create arc mode */
	private JRadioButton createArcButton;
	
	
	
	/** Singleton instance  */
	private static EditorPane instance;
	
	/** static constructor that initializes the instance */
	static {
		instance = new EditorPane();
	}
	
	/** Private default constructor */
	private EditorPane() {
		editorPane = new JPanel();
		getEditorPane().setBounds(EDITOR_PANE_X, 
				EDITOR_PANE_Y, 
				EDITOR_PANE_WIDTH, 
				EDITOR_PANE_HEIGHT);
		getEditorPane().setBorder(EDITOR_PANE_BORDER);
		getEditorPane().setLayout(EDITOR_PANE_LAYOUT);
		
		initiateRadioButton();
	}
	

	/**
	 * Returns the singleton instance
	 * @return
	 */
	public static EditorPane getInstance(){
		return instance;
	}
	
	/** Returns the internal JPanel */
	private JPanel getEditorPane(){
		return editorPane;
	}
	
	/**
	 * Adds the EditorPane to a frame
	 * @param frame
	 */
	public void addTo(JPanel frame){
		frame.add(getEditorPane());
	}
	
	/** Initiates a ButtonGroup with the 4 radio buttons in it */
	private void initiateRadioButton() {
		pickButton = initiateRadioPickButton();
		createArcButton = initiateRadioArcButton();
		createPlaceButton = initiateRadioPlaceButton();
		createTransitionButton = initiateRadioTransitionButton();
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(pickButton);
		buttonGroup.add(createArcButton);
		buttonGroup.add(createPlaceButton);
		buttonGroup.add(createTransitionButton);
	}

	/** Initiates the transition radio button with text and tool tip */
	private JRadioButton initiateRadioTransitionButton() {
		createTransitionButton = new JRadioButton("Stelle einfügen");
		createTransitionButton.setToolTipText("In diesem Modus fügen Sie mit einem Klick eine neue Transition hinzu");
		
		// the action command is used later on to identify wich button is selected
		createTransitionButton.setActionCommand("transition");
		getEditorPane().add(createTransitionButton);
		
		return createTransitionButton;
	}
	
	/** Initiates the place radio button with text and tool tip */
	private JRadioButton initiateRadioPlaceButton() {
		createPlaceButton = new JRadioButton("Stelle einfügen");
		createPlaceButton.setToolTipText("In diesem Modus fügen Sie mit einem Klick eine neue Stelle hinzu");
		
		// the action command is used later on to identify wich button is selected
		createPlaceButton.setActionCommand("place");
		getEditorPane().add(createPlaceButton);
		
		return createPlaceButton;
	}
	
	/** Initiates the arc radio button with text and tool tip */
	private JRadioButton initiateRadioArcButton() {
		createArcButton = new JRadioButton("Kante einfügen");
		createArcButton.setToolTipText("In diesem Modus fügen Sie mit einem Klick eine neue Kante hinzu");
		
		// the action command is used later on to identify wich button is selected
		createArcButton.setActionCommand("arc");
		getEditorPane().add(createArcButton);
		
		return createArcButton;
	}


	/** Initiates the pick radio button with text and tool tip */
	private JRadioButton initiateRadioPickButton(){
		pickButton = new JRadioButton("Auswählen");
		pickButton.setSelected(true);
		pickButton.setToolTipText("Editieren und verschieben Sie Stellen und Transitionen " +
				"indem sie auf sei klicken. Mit drag & drop auf eine weiße Stelle verschieben " +
				"sie das ganze Petrinetze");
		
		// the action command is used later on to identify wich button is selected
		pickButton.setActionCommand("pick");
		getEditorPane().add(pickButton);
		
		return pickButton;
	}
	
	/** Returns the mode that is currently selected */
	public EditorMode getCurrentMode(){
		return EditorMode.valueOf(buttonGroup.getSelection().getActionCommand().toUpperCase());
	}
	
	/** Enumerable for editor modes*/
	static enum EditorMode{
		PLACE, TRANSITION, ARC, PICK
	}

}


package transformation.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import petrinet.Petrinet;
import petrinet.Place;

import transformation.Rule;
import transformation.Transformation;
import transformation.TransformationComponent;
import data.Rule3Data;
import exceptions.GeneralPetrinetException;


/**
 * Testing if Rule 3 like specified in /../additional/images/Rule_3.png works
 * correctly
 * 
 */
public class Rule3Test {
	
	/** petrinet to transform */
	private static Petrinet nPetrinet = Rule3Data.getnPetrinet();
	/** rule to apply */
	private static Rule rule = Rule3Data.getRule();
	/** added place need for check equals*/
	private static Place newPlace = Rule3Data.getNewPlace();

	private static Transformation transformation;

	@BeforeClass
	public static void applyingRule() {
		try {
			transformation = TransformationComponent.getTransformation().transform(nPetrinet, rule);
		} catch (Exception e) {
			System.out.println(e);
			fail("Morphism should not be found, but no error should be raised.");
		}
	}
	
	@Test
	public void testRightMorphism() {
		assertFalse("nPetrinet should not be empty", nPetrinet.isEmpty());
		
		//find added place
		Place addedPlace = null;
		for (Place place : nPetrinet.getAllPlaces()) {
			if(place.getStartArcs().isEmpty() && place.getEndArcs().isEmpty()){
				addedPlace = place;
			}
		}
		
		assertNotNull("one place should have no arcs", addedPlace);
		assertNotSame("places should not be same", newPlace, addedPlace);
		assertEquals(8,nPetrinet.getAllPlaces().size());
	}
	


}
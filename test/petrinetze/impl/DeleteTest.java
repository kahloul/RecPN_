package petrinetze.impl;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;

import org.junit.*;

import petrinet.Petrinet;
import petrinet.PetrinetComponent;

import data.MorphismData;

/** Testing the not void delete method */
public class DeleteTest {
	
	private static Petrinet nPetrinet;
	
//	private static int toDeletePlace;
	
//	private static Collection<Integer> deleted;
	
	@Before
	public void initialize(){
		nPetrinet = MorphismData.getPetrinetIsomorphismPlacesTo();
	}
	
	@Test
	public void checkReturnValuePlace(){
		int toDeletePlace = MorphismData.getIdMatchesInRule2();
		
		Collection<Integer> deleted = PetrinetComponent.getPetrinet().deleteElementInPetrinet(nPetrinet.getId(), toDeletePlace);
		assertEquals(6,deleted.size());
		assertEquals(MorphismData.getIdsOfPlaceAndArcsOfThirdPlace(),
				new HashSet<Integer>(deleted));
	}
	
	@Test
	public void checkReturnValueTransition(){
		int toDeleteTransitionPre = MorphismData.getIdPreTransiotionOfThird();
		int toDeleteTransitionPost = MorphismData.getIdPostTransiotionOfThird();
		
		Collection<Integer> deletedPre = PetrinetComponent.getPetrinet().deleteElementInPetrinet(nPetrinet.getId(), toDeleteTransitionPre);
		assertEquals(2,deletedPre.size());
		assertEquals(MorphismData.getIdsOfTransitionPreAndArcsOfThirdPlace(),
				new HashSet<Integer>(deletedPre));
		
		Collection<Integer> deletedPost = PetrinetComponent.getPetrinet().deleteElementInPetrinet(nPetrinet.getId(), toDeleteTransitionPost);
		assertEquals(2,deletedPost.size());
		assertEquals(MorphismData.getIdsOfTransitionPostAndArcsOfThirdPlace(),
				new HashSet<Integer>(deletedPost));
	}
	
	@Test
	public void checkReturnValueArc(){
		int toDeleteArc = MorphismData.getIdOfDeleteArc();
		
		Collection<Integer> deletedArc = PetrinetComponent.getPetrinet().deleteElementInPetrinet(nPetrinet.getId(),toDeleteArc );
		assertEquals(1,deletedArc.size());
		assertEquals(deletedArc.iterator().next(),(Integer)toDeleteArc);
		Collection<Integer> deleteNothing = PetrinetComponent.getPetrinet().deleteElementInPetrinet(nPetrinet.getId(),toDeleteArc );
		assertTrue(deleteNothing.isEmpty());
	}

}
package data;

import java.util.HashSet;
import java.util.Set;


import petrinet.PetrinetComponent;
import petrinet.model.IArc;
import petrinet.model.IRenew;
import petrinet.model.Petrinet;
import petrinet.model.Place;
import petrinet.model.Transition;
import petrinet.model.rnw.Count;
import petrinet.model.rnw.Identity;


/**
 * This Class contains the test data for morphism test. 
 * 
 */
public class MorphismData {
	

	public static int getIdMatchesInRule2(){
		return idOfThird;
	}
	
	public static int getIdFromTransitions() {
		return idFromTransitions;
	}

	public static int getIdFromPlaces() {
		return idFromPlaces;
	}

	public static int getIdMatchedTransition() {
		return idMatchedTransition;
	}

	public static Set<Integer> getIdsMatchedPlaces() {
		return idsMatchedPlaces;
	}
	
	public static Set<Integer> getIdsOfPlaceAndArcsOfThirdPlace() {
		return idsOfPlaceAndArcsOfThirdPlace;
	}
	
	public static Set<Integer> getIdsOfTransitionPreAndArcsOfThirdPlace(){
		return idsOfTransitionPreAndArcsOfThirdPlace;
	}
	
	public static int getIdPreTransiotionOfThird(){
		return idPreTransiotionOfThird;
	}
	
	public static int getIdPostTransiotionOfThird(){
		return idPostTransiotionOfThird;
	}
	
	public static Set<Integer> getIdsOfTransitionPostAndArcsOfThirdPlace(){
		return idsOfTransitionPostAndArcsOfThirdPlace;
	}
	
	public static int getIdOfDeleteArc(){
		return idOfDeleteArc;
	}

	private static int idFromTransitions;

	private static int idFromPlaces;
	
	private static int idMatchedTransition;
	
	private static Set<Integer> idsMatchedPlaces = new HashSet<Integer>();
	
	private static int idOfThird;
	
	private static int idPreTransiotionOfThird;
	
	private static int idPostTransiotionOfThird;
	
	private static int idOfDeleteArc;
	
	private static Set<Integer> idsOfPlaceAndArcsOfThirdPlace = new HashSet<Integer>();
	
	private static Set<Integer> idsOfTransitionPreAndArcsOfThirdPlace = new HashSet<Integer>();
	
	private static Set<Integer> idsOfTransitionPostAndArcsOfThirdPlace = new HashSet<Integer>();
	
	private MorphismData(){}
	
	/**
	 * 
	 * @return the "from" Petrinet that is specified in... 
 	 */
	public static Petrinet getPetrinetIsomorphismPlacesFrom(){
		Petrinet result = PetrinetComponent.getPetrinet().createPetrinet();
		IRenew renewId = new Identity();
		
		Place p1 = result.addPlace("P1");
		
		idFromPlaces = p1.getId();
		
		Transition t1 = result.addTransition("A", renewId);
		Transition t2 = result.addTransition("A", renewId);
		Transition t3 = result.addTransition("A", renewId);
		Transition t4 = result.addTransition("A", renewId);
		Transition t5 = result.addTransition("A", renewId);
		
		//post
		result.addPostArc("", t1, p1);
		result.addPostArc("", t2, p1);

		// pre
		result.addPreArc("", p1, t3);
		result.addPreArc("", p1, t4);
		result.addPreArc("", p1, t5);
		
		//mark
		p1.setMark(2);
		
		return result;
	}
	
	
	/**
	 * 
	 * @return the "to" Petrinet that is specified in... 
 	 */
	public static Petrinet getPetrinetIsomorphismPlacesTo(){
		idsOfTransitionPostAndArcsOfThirdPlace = new HashSet<Integer>();
		idsOfTransitionPreAndArcsOfThirdPlace = new HashSet<Integer>();
		idsOfPlaceAndArcsOfThirdPlace = new HashSet<Integer>();
		idsMatchedPlaces = new HashSet<Integer>();
		
		// The matching subnet P1 and T1...
		Petrinet result = PetrinetComponent.getPetrinet().createPetrinet();
		IRenew renewId = new Identity();
		
		Place p1 = result.addPlace("P1");
		
		idsMatchedPlaces.add(p1.getId());
		
		Transition t11 = result.addTransition("A", renewId);
		Transition t12 = result.addTransition("A", renewId);
		Transition t13 = result.addTransition("A", renewId);
		Transition t14 = result.addTransition("A", renewId);
		Transition t15 = result.addTransition("A", renewId);
		
		//post
		result.addPostArc("", t11, p1);
		result.addPostArc("", t12, p1);

		// pre
		result.addPreArc("", p1, t13);
		result.addPreArc("", p1, t14);
		result.addPreArc("", p1, t15);
		
		//mark
		p1.setMark(2);
		
		
		
		// The not matching subnet mark not enough  
		// with following int 2
		Place p2 = result.addPlace("P1");
				
		Transition t21 = result.addTransition("A", renewId);
		Transition t22 = result.addTransition("A", renewId);
		Transition t23 = result.addTransition("A", renewId);
		Transition t24 = result.addTransition("A", renewId);
		Transition t25 = result.addTransition("A", renewId);

		//post
		result.addPostArc("", t21, p2);
		result.addPostArc("", t22, p2);

		// pre
		result.addPreArc("", p2, t23);
		result.addPreArc("", p2, t24);
		result.addPreArc("", p2, t25);
				
		//mark
		p2.setMark(1);
		
		
		// The matching subnet mark is 1 more  
		// with following int 3
		Place p3 = result.addPlace("P1");
		idOfThird = p3.getId();
		idsMatchedPlaces.add(idOfThird);
		idsOfPlaceAndArcsOfThirdPlace.add(idOfThird);

				
		Transition t31 = result.addTransition("A", renewId);
		idPreTransiotionOfThird = t31.getId();
		idsOfTransitionPreAndArcsOfThirdPlace.add(idPreTransiotionOfThird);
		Transition t32 = result.addTransition("A", renewId);
		Transition t33 = result.addTransition("A", renewId);
		idPostTransiotionOfThird = t33.getId();
		idsOfTransitionPostAndArcsOfThirdPlace.add(idPostTransiotionOfThird);
		Transition t34 = result.addTransition("A", renewId);
		Transition t35 = result.addTransition("A", renewId);
		
		
		
				
		// pre
		IArc arcPlace31 = result.addPostArc("", t31, p3);
		idOfDeleteArc = arcPlace31.getId();
		idsOfPlaceAndArcsOfThirdPlace.add(idOfDeleteArc);
		idsOfTransitionPreAndArcsOfThirdPlace.add(idOfDeleteArc);
		IArc arcPlace32 = result.addPostArc("", t32, p3);
		idsOfPlaceAndArcsOfThirdPlace.add(arcPlace32.getId());
				
		//post
		IArc arcPlace33 = result.addPreArc("", p3, t33);
		idsOfPlaceAndArcsOfThirdPlace.add(arcPlace33.getId());
		idsOfTransitionPostAndArcsOfThirdPlace.add(arcPlace33.getId());
		IArc arcPlace34 = result.addPreArc("", p3, t34);
		idsOfPlaceAndArcsOfThirdPlace.add(arcPlace34.getId());
		IArc arcPlace35 = result.addPreArc("", p3, t35);
		idsOfPlaceAndArcsOfThirdPlace.add(arcPlace35.getId());
				
		//mark
		p3.setMark(3);
		
		
		// The not matching subnet pre is not enough  
		// with following int 4
		Place p4 = result.addPlace("P1");
						
		Transition t41 = result.addTransition("A", renewId);
		Transition t43 = result.addTransition("A", renewId);
		Transition t44 = result.addTransition("A", renewId);
		Transition t45 = result.addTransition("A", renewId);
						
		// pre
		result.addPostArc("", t41, p4);
						
		//post
		result.addPreArc("", p4, t43);
		result.addPreArc("", p4, t44);
		result.addPreArc("", p4, t45);
						
		//mark
		p4.setMark(2);
		
		
		// The not matching subnet post is not enough  
		// with following int 5
		Place p5 = result.addPlace("P1");
				
		Transition t51 = result.addTransition("A", renewId);
		Transition t52 = result.addTransition("A", renewId);
		Transition t54 = result.addTransition("A", renewId);
		Transition t55 = result.addTransition("A", renewId);
				
		// pre
		result.addPostArc("", t51, p5);
		result.addPostArc("", t52, p5);
				
		//post
		result.addPreArc("", p5, t54);
		result.addPreArc("", p5, t55);
				
		//mark
		p5.setMark(2);
		
		
		// The matching subnet pre is to many  
		// with following int 6
		Place p6 = result.addPlace("P1");
		idsMatchedPlaces.add(p6.getId());

						
		Transition t61 = result.addTransition("A", renewId);
		Transition t62 = result.addTransition("A", renewId);
		Transition t63 = result.addTransition("A", renewId);
		Transition t64 = result.addTransition("A", renewId);
		Transition t65 = result.addTransition("A", renewId);
		Transition t66 = result.addTransition("A", renewId);
						
		// pre
		result.addPostArc("", t61, p6);
		result.addPostArc("", t62, p6);
		result.addPostArc("", t63, p6);
						
		//post
		result.addPreArc("", p6, t64);
		result.addPreArc("", p6, t65);
		result.addPreArc("", p6, t66);
						
		//mark
		p6.setMark(2);
		
		
		// The matching subnet post is to many  
		// with following int 7
		Place p7 = result.addPlace("P1");
		idsMatchedPlaces.add(p7.getId());

								
		Transition t71 = result.addTransition("A", renewId);
		Transition t72 = result.addTransition("A", renewId);
		Transition t73 = result.addTransition("A", renewId);
		Transition t74 = result.addTransition("A", renewId);
		Transition t75 = result.addTransition("A", renewId);
		Transition t76 = result.addTransition("A", renewId);
								
		// pre
		result.addPostArc("", t71, p7);
		result.addPostArc("", t72, p7);
								
		//post
		result.addPreArc("", p7, t73);
		result.addPreArc("", p7, t74);
		result.addPreArc("", p7, t75);
		result.addPreArc("", p7, t76);
								
		//mark
		p7.setMark(2);
		
		
		return result;
	}
	
	
	/**
	 * Returns the "from" petrinet specified in '../additional/images/Isomorphism_transitions.png'
	 */
	public static Petrinet getPetrinetIsomorphismTransitionsFrom(){
		Petrinet result = PetrinetComponent.getPetrinet().createPetrinet();
		
		IRenew rnwId = new Identity();
		
		idFromTransitions = addSubnetToPetrinetLikeInMorphismTransition(result, "P1".split(" "), "P2 P3".split(" "), "A", rnwId, "1");
		
		return result;
	}

	
	/**
	 * Returns the "to" petrinet specified in '../additional/images/Isomorphism_transitions.png'
	 */
	public static Petrinet getPetrinetIsomorphismTransitionsTo(){
		Petrinet result = PetrinetComponent.getPetrinet().createPetrinet();
		
		IRenew rnwId = new Identity();
		IRenew rnwCount = new Count();
		
		String[] pre1 = {"P1"};
		String[] pre2 = {"P1"};
		String[] pre3 = {"P1"};
		String[] pre4 = {"P1"};
		String[] pre5 = {"P1"};
		String[] pre6 = {"P1", "P2"};
		String[] pre7 = {"P1"};
		String[] pre8 = {};
		
		
		String[] post1 = {"P2", "P3"};
		String[] post2 = {"P2", "P3"};
		String[] post3 = {"P2", "P3"};
		String[] post4 = {"P2", "P3"};
		String[] post5 = {"P2", "P3", "P4"};
		String[] post6 = {"P2", "P3"};
		String[] post7 = {"P2"};
		String[] post8 = {"P2", "P3"};
		
		String[] name = {"A", "A", "A", "B", "A", "A", "A", "A"};
		String[] tlb =  {"1", "1", "2", "1", "1", "1", "1", "1"};
		
		idMatchedTransition = addSubnetToPetrinetLikeInMorphismTransition(result,pre1,post1,name[0],rnwId,tlb[0]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre2,post2,name[1],rnwCount,tlb[1]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre3,post3,name[2],rnwId,tlb[2]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre4,post4,name[3],rnwId,tlb[3]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre5,post5,name[4],rnwId,tlb[4]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre6,post6,name[5],rnwId,tlb[5]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre7,post7,name[6],rnwId,tlb[6]);
		addSubnetToPetrinetLikeInMorphismTransition(result,pre8,post8,name[7],rnwId,tlb[7]);
		return result;
	}
	
	
	private static int addSubnetToPetrinetLikeInMorphismTransition(Petrinet petrinet, 
			String[] pre, 
			String[] post, 
			String name, 
			IRenew renew, 
			String tlb){
		Transition transition = petrinet.addTransition(name,renew);
		transition.setTlb(tlb);
		for (String string: pre) {
			Place place = petrinet.addPlace(string);
			petrinet.addPreArc("", place, transition);
		}
		for (String string : post) {
			Place place = petrinet.addPlace(string);
			petrinet.addPostArc("", transition, place);
		}
		return transition.getId();
	}
	
	
//	public static void main(String[] args){
//		Petrinet from = MorphismData.getPetrinetIsomorphismPlacesFrom();
//		System.out.println(from.toString());
		
//		Petrinet to = MorphismData.getPetrinetIsomorphismPlacesTo();
//		System.out.println(to.toString());
		
//	}
	

}

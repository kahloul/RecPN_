/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transformation;


import java.util.Set;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import petrinetze.*;

import petrinetze.impl.RenewCount;

/**
 *
 * @author Niklas, Philipp K�hn
 */
public class RuleTest {

    public IRule rule1;

    public RuleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        rule1 = new Rule();
        //L von r1
        IPlace p1 = rule1.L().createPlace("Wecker ein");
        IPlace p2 = rule1.K().createPlace("Wecker ein");
        p2.setMark(1);
        IPlace p3 = rule1.K().createPlace("");
        IPlace p4 = rule1.K().createPlace("");
        IPlace p5 = rule1.K().createPlace("Wecker aus");
        IPlace p6 = rule1.L().createPlace("Wecker aus");
        ITransition t1 = rule1.L().createTransition("", Renews.COUNT);
        rule1.L().createArc("", p1, t1);
        rule1.L().createArc("", t1, rule1.fromKtoL(p4));
        ITransition t2 = rule1.L().createTransition("", Renews.COUNT);
        rule1.L().createArc("", rule1.fromKtoL(p3), t2);
        rule1.L().createArc("", t2, p6);
        ITransition t3 = rule1.R().createTransition("", Renews.COUNT);
        rule1.R().createArc("", rule1.fromKtoR(p2), t3);
        rule1.R().createArc("", t3, rule1.fromKtoR(p4));
        ITransition t4 = rule1.R().createTransition("", Renews.COUNT);
        rule1.R().createArc("", rule1.fromKtoR(p3), t4);
        rule1.R().createArc("", t4, rule1.fromKtoR(p5));
  
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of fromLtoK method, of class IRule.
     */
    @Test
    public void testFromLtoK_INode() {
        System.out.println("fromLtoK");
        IRule instance = new Rule();
        INode node = instance.L().createPlace("a");
        assertEquals(node.getName(), instance.fromLtoK(node).getName());
    	assertEquals(node.getName(), instance.fromKtoL(instance.fromLtoK(node)).getName());
    }

    /**
     * Test of fromLtoK method, of class IRule.
     */
    @Test
    public void testFromLtoK_IArc() {
        System.out.println("fromLtoK");
        IRule instance = new Rule();
        ITransition node = instance.L().createTransition("a", Renews.COUNT);
        assertEquals(node.getName(), instance.fromLtoK(node).getName());
        assertEquals(node.getRnw(), ((ITransition)instance.fromLtoK(node)).getRnw());
    	assertEquals(node.getName(), instance.fromKtoL(instance.fromLtoK(node)).getName());
    	assertEquals(node.getRnw(), ((ITransition)instance.fromKtoL(instance.fromLtoK(node))).getRnw());
    }

    /**
     * Test of fromRtoK method, of class IRule.
     */
    @Test
    public void testFromRtoK_INode() {
        System.out.println("fromRtoK");
        IRule instance = new Rule();
        INode node = instance.R().createPlace("a");
        assertEquals(node.getName(), instance.fromRtoK(node).getName());
    	assertEquals(node.getName(), instance.fromKtoR(instance.fromRtoK(node)).getName());
    }

    /**
     * Test of fromRtoK method, of class IRule.
     */
    @Test
    public void testFromRtoK_IArc() {
        System.out.println("fromRtoK");
        IRule instance = new Rule();
        ITransition node = instance.R().createTransition("a", Renews.COUNT);
        assertEquals(node.getName(), instance.fromRtoK(node).getName());
        assertEquals(node.getRnw(), ((ITransition)instance.fromRtoK(node)).getRnw());
    	assertEquals(node.getName(), instance.fromKtoR(instance.fromRtoK(node)).getName());
    	assertEquals(node.getRnw(), ((ITransition)instance.fromKtoR(instance.fromRtoK(node))).getRnw());
    }

    @Test
    public void testPlaces(){
        System.out.println("Places");
        Set<IPlace> k = rule1.K().getAllPlaces();
        Set<IPlace> l = rule1.L().getAllPlaces();
        Set<IPlace> r = rule1.R().getAllPlaces();
        for(IPlace place : r){
            assertTrue(k.contains(rule1.fromRtoK(place)));
        }
        for(IPlace place : l){
            assertTrue(k.contains(rule1.fromLtoK(place)));
        }
    }

    @Test
    public void testArcs(){
        System.out.println("Arcs");
        Set<IArc> k = rule1.K().getAllArcs();
        Set<IArc> l = rule1.L().getAllArcs();
        Set<IArc> r = rule1.R().getAllArcs();
        for(IArc place : r){
            assertTrue(k.contains(rule1.fromRtoK(place)));
        }
        for(IArc place : l){
            assertTrue(k.contains(rule1.fromLtoK(place)));
        }
    }

    @Test
    public void testTransitions(){
        System.out.println("Transitions");
        Set<ITransition> k = rule1.K().getAllTransitions();
        Set<ITransition> l = rule1.L().getAllTransitions();
        Set<ITransition> r = rule1.R().getAllTransitions();
        for(ITransition place : r){
            assertTrue(k.contains(rule1.fromRtoK(place)));
        }
        for(ITransition place : l){
            assertTrue(k.contains(rule1.fromLtoK(place)));
        }
    }
}
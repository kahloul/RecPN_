package persistence;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import petrinet.INode;
import petrinet.Petrinet;
import engine.attribute.NodeLayoutAttribute;
import engine.ihandler.IPetrinetManipulation;

public class PersistanceUtility implements IPersistance {

	@Override
	public boolean save(String pathAndFilename, Petrinet petrinet,
			Map<INode, NodeLayoutAttribute> nodeMap) {

		/*		    
	    Pnml pnml2=Converter.convertToPnml(petrinet, layoutMap);
	    Marshaller ma=context.createMarshaller();
	    File f=new File("./testPNMLFile.txt");
	    ma.marshal(pnml2, f);
	    System.out.print("file:"+f);*/
		
		return false;
	}

	@Override
	public int load(String pathAndFilename, IPetrinetManipulation handler) {
		Pnml pnml=new Pnml();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance( persistence.Pnml.class , Arc.class, Converter.class, Dimension.class, Graphics.class, InitialMarking.class, Name.class, Net.class,
					Page.class, Place.class, PlaceName.class, Position.class, Transition.class, TransitionLabel.class, TransitionName.class, TransitionRenew.class);
		    Unmarshaller m = context.createUnmarshaller();
		    
		    m.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		    
		    pnml=(Pnml)m.unmarshal(new File(pathAndFilename));
		    
		    boolean success = Converter.convertToPetrinet(pnml, handler);
			/*
		    System.out.println(petrinet.getAllArcs());
		    System.out.println("got here");
		    Map<String, String[]> layoutMap=new HashMap<String, String[]>();
		    System.out.println("got here");
		    int i=0;
		    for(petrinet.Place p:petrinet.getAllPlaces()){
		    	String[] pos=new String[]{String.valueOf(i),String.valueOf(i)};
		    	layoutMap.put(String.valueOf(p.getId()), pos);
		    	i++;
		    }*/
		    return success ? 1 : 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
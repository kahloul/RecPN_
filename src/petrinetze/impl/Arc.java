package petrinetze.impl;


import petrinetze.IArc;
import petrinetze.INode;

/**
* Diese Klasse stellt eine Kante in Petrinetze dar und
* bietet die dazu geh�rige Methoden dafuer an.
* 
* @author Reiter, Safai
* @version 1.0
*/

public class Arc implements IArc{

	/**
	 *  Die maximal m�gliche Kantengewichtung.
	 */
	private int mark = 1;
	private String name;
	
	/**
	 *  Der Startknoten der Kante.
	 */
	private INode start;
	
	/**
	 * 	Der Endknoten der Kante.
	 */
	private INode end;

	private int id;
	
	
	public Arc(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}


	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#getMark()
	 */
	@Override
	public int getMark() {
		return mark;
	}
	
	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#setMark(int)
	 */
	@Override
	public void setMark(int mark) {
		if (mark > 0) {
			this.mark = mark;
		}
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#getStart()
	 */
	@Override
	public INode getStart() {
		return start;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#getEnd()
	 */
	@Override
	public INode getEnd() {
		return end;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#setStart(haw.wp.rcpn.INode)
	 */
	@Override
	public void setStart(INode start) throws IllegalArgumentException
 {
		if (!isValidPrecondition(start)) {
			throw new IllegalArgumentException("Start und Ende muessen unterschiedliche Knotenarten haben");
		}
		this.start = start;
		//Jede StartKante registriert sich bei dem entsprechenden
		//INode als Startkante, damit die Berechnung von Pre und
		//Post von statten gehen kann.
		if (start instanceof Place) {
			((Place) this.start).setStartArcs(this.id);
		} else {
			((Transition) this.start).setStartArcs(this.id);
		}
		
	}

	private boolean isValidPrecondition(INode start) {
		
		if (start instanceof Place && end instanceof Place) {
			return false;
		}
		if (start instanceof Transition && end instanceof Transition) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see haw.wp.rcpn.impl.IArc#setEnd(haw.wp.rcpn.INode)
	 */
	@Override
	public void setEnd(INode end) throws IllegalArgumentException
{
		if (!isValidPrecondition(end)) {
			throw new IllegalArgumentException("Start und Ende muessen unterschiedliche Knotenarten haben");
		}
		this.end = end;
		//Jede EndKante registriert sich bei dem entsprechenden
		//INode als Endkante, damit die Berechnung von Pre und
		//Post von statten gehen kann.
		if (end instanceof Place) {
			((Place) this.end).setEndArcs(this.id);
		} else {
			((Transition) this.end).setEndArcs(this.id);
		}
		this.end = end;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Arc [mark=" + mark + ", name=" + name + ", start=" + start
				+ ", end=" + end + ", id=" + id + "]";
	}

	
}

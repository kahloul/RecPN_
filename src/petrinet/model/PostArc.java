package petrinet.model;

/**
 * The arc of a petrinet. Holding information about its marking as well as
 * references to its source and target nodes.
 */
public final class PostArc implements IArc {
	/**
	 * Maximal weight
	 */
	private int weight;

	/**
	 * Name of the arc
	 */
	private String name;

	/**
	 * target transition
	 */
	private Transition source;

	/**
	 * source place
	 */
	private Place target;

	/**
	 * Unique id
	 */
	private final int id;

	/**
	 * Creates a new arc
	 * 
	 * @param id       Unique id
	 * @param source   source node of arc
	 * @param target   target node of arc
	 * @param name     name of arc
	 * @param weight   arc weight
	 */
	public PostArc(int id, Transition source, Place target, String name, int weight) {		
		this.id       = id;
		this.source   = source;
		this.target   = target;
		this.name     = name;
		this.weight   = weight;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#getId()
	 */
	@Override
	public int getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#getWeight()
	 */
	@Override
	public int getWeight() {
		return weight;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#setWeight(int)
	 */
	@Override
	public void setWeight(int weight) {
		if (weight <= 0) {
			throw new IllegalArgumentException();
		}
		
		this.weight = weight;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#getSource()
	 */
	@Override
	public Transition getSource() {
		return source;
	}

	public Transition getTransition() {
		return source;
	}

	/* (non-Javadoc)
	 * @see petrinet.model.IArc#getTarget()
	 */
	@Override
	public Place getTarget() {
		return target;
	}

	public Place getPlace() {
		return target;
	}


	@Override
	public String toString() {
		return "PostArc [weight=" + weight + ", name=" + name + ", transition=" + source
				+ ", place=" + target + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		
		if (!(object instanceof PostArc)) {
			return false;
		}
		
		PostArc arc = (PostArc) object;
		
		return id == arc.getId();
	}	
}
package petrinet;

import java.util.HashMap;
import java.util.Map;

/**
 * Renew that transforms the label as specified in a map. The Label is
 * represented as {@link String}. The guiString is "map (not supported)".
 * <b>This Renew is not yet supported on the GUI</b>
 * 
 */
public class RenewMap implements IRenew {

    private final HashMap<String,String> rnw;

	public RenewMap(final Map<String, String> rnw) {
		this.rnw = new HashMap<String, String>(rnw);
	}
	
	@Override
	public String renew(String tlb) {
		return rnw.get(tlb);
	}

	@Override
	public boolean isTlbValid(String tlb) {
		return rnw.containsKey(tlb);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getClass().hashCode() ^ rnw.hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        return this.rnw.equals(((RenewMap)obj).rnw);
	}
	
	@Override
	public String toGUIString() {
		return "map (not supported)";
	}
}

package petrinet;

public interface IRenew {

    public String renew (String tlb);

    boolean isTlbValid(String tlb);
    
    String toGUIString();
}

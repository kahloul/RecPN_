package petrinet;

import java.util.Map;

public final class Renews {

    private Renews() {}

    public static final IRenew IDENTITY = new RenewId();

    public static final IRenew COUNT = new RenewCount();

    public static final IRenew fromMap(Map<String,String> f) {
    	
        return new RenewMap(f);
    }
}
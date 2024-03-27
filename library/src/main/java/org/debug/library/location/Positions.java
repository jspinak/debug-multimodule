package org.debug.library.location;

import java.util.HashMap;
import java.util.Map;

public class Positions {

    public enum Name {
        TOPLEFT, TOPMIDDLE, TOPRIGHT, MIDDLELEFT, MIDDLEMIDDLE, MIDDLERIGHT, BOTTOMLEFT, BOTTOMMIDDLE, BOTTOMRIGHT
    }

    private final static Map<Name, Double> positions = new HashMap<>();
    
    public static Double getCoordinates(Name position) {
        return positions.get(position);
    }
    
}

package processing.utility;

public class BrushRadius {
	public double brushRadius; // The Brush Radius
	
	// The equals method
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BrushRadius)
			return brushRadius == ((BrushRadius)obj).brushRadius;
		else
			return false;
	}
}

package UI;

import java.util.ArrayList;
import processing.utility.Pixel;

public class model {
    private final UpdateMode identifier ;
    private final ArrayList<Pixel> pixelArray ;
    private final String Message;

    public model(UpdateMode identifier, ArrayList<Pixel> pixelArray) {
        this.Message = "";
		this.identifier = identifier ;
        this.pixelArray = pixelArray ;
    }
    
    public model(UpdateMode identifier, String message) {
        this.pixelArray = new ArrayList<Pixel>();
		this.identifier = identifier ;
        this.Message = message ;
    }

    public String getMessage() {
        return Message ;
    }

    public ArrayList<Pixel> getPixelArray() {
        return pixelArray ;
    }
    
    public UpdateMode getIdentifier() {
    	return identifier;
    }
}
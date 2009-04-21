package handlers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EnergyHandler extends NodeHandler{

	private Double energy;
	private boolean hasEnergy = false;
	
	public Double getEnergy() {
		return energy;
	}
	
	public boolean getHasEnergy(){
		return hasEnergy;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String e = new String(ch,start,length);
		energy = Double.parseDouble(e);
		if (e != null) {
			hasEnergy = true;
		}
		else {
			System.err.println("An Energy value is missing");
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
	throws SAXException {
	}
}

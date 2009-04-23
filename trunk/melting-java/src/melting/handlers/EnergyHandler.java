package melting.handlers;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EnergyHandler extends NodeHandler{

	private double energy;
	private boolean hasEnergy = false;
	
	public double getEnergy() {
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
		hasEnergy = true;
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
	throws SAXException {
		if (hasEnergy){
			completedNode();
		}
		else {
			throw new SAXException("a enthalpy or entropy value is missing, the node is incomplete");
		}
	}
}

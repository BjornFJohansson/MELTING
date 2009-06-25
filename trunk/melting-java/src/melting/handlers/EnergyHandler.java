package melting.handlers;


import melting.exceptions.ThermodynamicParameterError;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EnergyHandler extends NodeHandler{

	private double energy;
	private boolean hasEnergy = false;
	private StringBuilder stringenergy = new StringBuilder();
	
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
		stringenergy.append(e);
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
	throws SAXException {
		try {
			if (stringenergy.length() != 0){
				energy = Double.parseDouble(stringenergy.toString());
				hasEnergy = true;
			}
		} catch (NumberFormatException e2) {
			throw new ThermodynamicParameterError("There is one error in the files containing the thermodynamic parameters. The energy value must be a numeric value.");
		}
		if (hasEnergy){
			completedNode();
		}
		else {
			throw new ThermodynamicParameterError("one enthalpy or entropy value is missing, the xml node is incomplete");
		}
	}
}

package handlers;

import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NeighborHandler extends ThermoHandler{
	
	private String sequence;
	
	public String getSequence() {
		return sequence;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (subHandler == null) {
			if (localName.equals("neighbor")) {
				sequence = attributes.getValue("sequence");
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (localName.equals("neighbor")){
			if (hasEnthalpy && hasEntropy){
				thermo = new Thermodynamics(enthalpy, entropy);
			}
		}
	}
}

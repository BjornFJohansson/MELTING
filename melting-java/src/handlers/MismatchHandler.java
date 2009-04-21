package handlers;


import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MismatchHandler extends ThermoHandler{
	
private String sequence;
private int size;
private String closing;
private String type;
private String loop;
	
public int getSize() {
	return size;
}

public String getClosing() {
	return closing;
}

public String getType() {
	return type;
}

public String getLoop() {
	return loop;
}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (subHandler == null) {
			if (localName.equals("mismatch")) {
				sequence = attributes.getValue("sequence");
				size = Integer.getInteger(attributes.getValue("size"));
				closing = attributes.getValue("closing");
				type = attributes.getValue("type");
				loop = attributes.getValue("loop");
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (localName.equals("mismatch")){
				thermo = new Thermodynamics(enthalpy, entropy);
		}
	}
}

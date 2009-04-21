package handlers;

import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class InitiationHandler extends ThermoHandler{

	private String type;
		
	public String getType() {
		return type;
	}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, name, attributes);
			if (subHandler == null) {
				if (localName.equals("initiation")) {
					type = attributes.getValue("type");
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			super.endElement(uri, localName, name);
			if (localName.equals("initiation")){
					thermo = new Thermodynamics(enthalpy, entropy);
			}
		}
}

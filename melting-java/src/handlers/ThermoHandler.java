package handlers;

import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ThermoHandler extends NodeHandler{
	
	protected Double enthalpy;
	protected Double entropy;
	protected Thermodynamics thermo;
	protected boolean hasEnthalpy = false;
	protected boolean hasEntropy = false;
	
	public Thermodynamics getThermodynamics() {
		return thermo;
	}
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (subHandler == null) {
			if (localName.equals("enthalpy")) {
				subHandler = new EnergyHandler();
				hasEnthalpy = true;
			}
			else if (localName.equals("entropy")) {
				subHandler = new EnergyHandler();
				hasEntropy = true;
			}
			else if (localName.equals("neighbor")) {
				sequence = attributes.getValue("sequence");
			}
		}
		else {
			subHandler.startElement(uri, localName, name, attributes);
			}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (subHandler != null) {
			subHandler.endElement(uri, localName, name);
			if (subHandler.hasCompletedNode()) {
				if (localName.equals("enthalpy")) {
					EnergyHandler HHandler = (EnergyHandler) subHandler;
					this.enthalpy = HHandler.getEnergy();
				} 
				else if (localName.equals("entropy")) {
					EnergyHandler SHandler = (EnergyHandler) subHandler;
					this.entropy = SHandler.getEnergy();
				}
				subHandler = null;
			}
		} 
		else {
			completedNode();
		}
	}
}

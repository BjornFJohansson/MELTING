package melting.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import melting.Thermodynamics;
import melting.exceptions.ThermodynamicParameterError;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ThermoHandler extends NodeHandler{
	
	private Double enthalpy;
	private Double entropy;
	private Thermodynamics thermo;
	private boolean hasEnthalpy = false;
	private boolean hasEntropy = false;
	private HashMap<String, String> attribut = new HashMap<String, String>();
	private String name;
	
	public String getName() {
		return name;
	}

	public Double getEnthalpy() {
		return enthalpy;
	}

	public Double getEntropy() {
		return entropy;
	}

	public Thermodynamics getThermo() {
		return thermo;
	}

	public HashMap<String, String> getAttribut() {
		return attribut;
	}

	public ThermoHandler(String nodeName){
		
	this.hasEnthalpy = false;
	this.hasEntropy = false; 
	this.name = nodeName;
	}
	
	public void initializeAttributes(){
		for (Iterator<Entry<String, String>> i = attribut.entrySet().iterator(); i.hasNext();){
			i.next().setValue(null);
		}
		this.thermo = null;
		this.enthalpy = 0.0;
		this.entropy = 0.0;
		this.hasEnthalpy = false;
		this.hasEntropy = false;
		this.name = null;
	}
	
	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (subHandler == null) {
			if (name.equals("enthalpy")) {
				subHandler = new EnergyHandler();
				hasEnthalpy = true;
			}
			else if (name.equals("entropy")) {
				subHandler = new EnergyHandler();
				hasEntropy = true;
			}
			else if (name.equals(this.name)){
				for (int i = 0;i < attributes.getLength(); i++ ){
					attribut.put(attributes.getQName(i), attributes.getValue(i));
				}
			}
			else {
				throw new ThermodynamicParameterError("The node " + name + " in the xml file is not known.");
			}
		}
		else {
			subHandler.startElement(uri, localName, name, attributes);
			}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (subHandler != null) {
			subHandler.characters(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (subHandler != null) {
			subHandler.endElement(uri, localName, name);
			if (subHandler.hasCompletedNode()) {
				EnergyHandler handler = (EnergyHandler) subHandler;
				
				if (name.equals("enthalpy")) {
					this.enthalpy = handler.getEnergy();
				} 
				else if (name.equals("entropy")) {
					this.entropy = handler.getEnergy();
				}
				else {
					throw new ThermodynamicParameterError("The node " + name + " in the xml file is not known.");
				}
				subHandler = null;
			}
		} 
		else {
			if (name.equals(this.name)) {
				thermo = new Thermodynamics(enthalpy, entropy);
			}	
			if (hasEnthalpy == false && hasEntropy == false) {
				throw new ThermodynamicParameterError("No energy value is entered for this xml node");
			}
			else if (hasEnthalpy == false && (this.name.equals("mismatch") == false  && this.name.equals("hairpin") == false && this.name.equals("bulge") == false)) {
				throw new ThermodynamicParameterError("No enthalpy value is entered for this xml node");
			}
			else if (hasEnthalpy == false && (this.name.equals("mismatch") == true || this.name.equals("bulge") == true || this.name.equals("hairpin") == true) && ((this.attribut.containsKey("size") && this.attribut.get("type") == "initiation") || this.attribut.containsKey("size") == false)) {
				throw new ThermodynamicParameterError("No enthalpy value is entered for this xml node");
			}
			else {
				if (thermo != null){
					completedNode();
				}
			}
		}
	}
}

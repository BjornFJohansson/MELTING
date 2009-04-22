package handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import melting.Thermodynamics;

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

	public boolean isHasEnthalpy() {
		return hasEnthalpy;
	}

	public boolean isHasEntropy() {
		return hasEntropy;
	}

	public HashMap<String, String> getAttribut() {
		return attribut;
	}

	public ThermoHandler(String nodeName){
		if (nodeName == "neighbor"){
			attribut.put("sequence",null);
		}
		else if (nodeName == "initiation"){
			attribut.put("type",null);
		}
		else if (nodeName == "terminal"){
			attribut.put("type",null);
		}
		else if (nodeName == "penalty"){
			attribut.put("type",null);
		}
		else if (nodeName == "closure"){
			attribut.put("type",null);
		}
		else if (nodeName == "mismatch"){
			attribut.put("type",null);
			attribut.put("sequence",null);
			attribut.put("size",null);
			attribut.put("loop",null);
			attribut.put("closing",null);	
		}
		else if (nodeName == "dangling"){
			attribut.put("sequence",null);
			attribut.put("sens",null);	
		}
		else if (nodeName == "modified"){
			attribut.put("type",null);
			attribut.put("sequence",null);
			attribut.put("sens",null);	
		}
		else if (nodeName == "bonus"){
			attribut.put("type",null);
			attribut.put("sequence",null);
			attribut.put("size",null);	
		}
		else if (nodeName == "CNG"){
			attribut.put("repeats",null);
			attribut.put("sequence",null);	
		}
		else if (nodeName == "parameter"){
			attribut.put("sequence",null);	
		}
		else if (nodeName == "bulge"){
			attribut.put("type",null);
			attribut.put("sequence",null);
			attribut.put("size",null);	
		}
		else if (nodeName == "hairpin"){
			attribut.put("type",null);
			attribut.put("sequence",null);
			attribut.put("size",null);	
		}
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
			else if (localName.equals(this.name)){
				for (Iterator<Entry<String, String>> i = attribut.entrySet().iterator(); i.hasNext();){
					Entry<String, String> entry = i.next();
					entry.setValue(attributes.getValue(entry.getKey()));
				}
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
				else if (localName.equals(this.name)) {
					thermo = new Thermodynamics(enthalpy, entropy);
				}
				subHandler = null;
			}
		} 
		else {
			completedNode();
		}
	}
}

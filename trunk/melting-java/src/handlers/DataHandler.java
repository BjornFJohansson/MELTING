package handlers;

import java.util.HashMap;

import melting.FileReader;
import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class DataHandler extends NodeHandler {
		private String type;
		private HashMap<String, Thermodynamics> map = new HashMap<String, Thermodynamics>();
		
		public HashMap<String, Thermodynamics> getMap() {
			return map;
		}

		public String getType(){
			return type;
		}
		
		private void setType(String s){
			this.type = s;
		}

		@Override
		public void startElement(String uri, String localName, String name,
				Attributes attributes) throws SAXException {
			// If there is no subnode
			if (subHandler == null) {
				if (localName.equals("datas")){
					this.setType(attributes.getValue("type"));
				}
				else if (localName.equals("initiation")){
					subHandler = new InitiationHandler();
				}
				else if (localName.equals("neighbor")){
					subHandler = new NeighborHandler();
				}
				else if (localName.equals("symetry")){
					subHandler = new SymetryHandler();
				}
				else if (localName.equals("terminal")){
					subHandler = new TerminalHandler();
				}
				else if (localName.equals("mismatch")){
					subHandler = new MismatchHandler();
				}
				else if (localName.equals("parameters")){
					subHandler = new MismatchHandler();
				}
				else if (localName.equals("closure")){
					subHandler = new ClosureHandler();
				}
				else if (localName.equals("penalty")){
					subHandler = new PenaltyHandler();
				}
				else if (localName.equals("asymetry")){
					subHandler = new AsymetryHandler();
				}
				else if (localName.equals("modified")){
					subHandler = new ModifiedHandler();
				}
				else if (localName.equals("dangling")){
					subHandler = new DanglingHandler();
				}
				else if (localName.equals("bulge")){
					subHandler = new BulgeHandler();
				}
				else if (localName.equals("hairpin")){
					subHandler = new HairpinHandler();
				}
				else if (localName.equals("bonus")){
					subHandler = new BonusHandler();
				}
				else if (localName.equals("CNG")){
					subHandler = new CNGHandler();
				}
			}
			else {
				subHandler.startElement(uri, localName, name, attributes);
			}
		}
		
		public void initializeNode(ThermoHandler nHandler){
			nHandler.thermo = null;
			nHandler.enthalpy = 0.0;
			nHandler.entropy = 0.0;
			nHandler.hasEnthalpy = false;
			nHandler.hasEntropy = false;
			subHandler = null;
		}

		@Override
		public void endElement(String uri, String localName, String name)
				throws SAXException {
			if (subHandler != null) {
				subHandler.endElement(uri, localName, name);
				if (subHandler.hasCompletedNode()) {
					if (subHandler instanceof NeighborHandler) {
						NeighborHandler nHandler = (NeighborHandler) subHandler;
						map.put(nHandler.getSequence(), nHandler.getThermodynamics());
						initializeNode(nHandler);	
					} 
					else if (subHandler instanceof InitiationHandler) {
						InitiationHandler iHandler = (InitiationHandler) subHandler;
						map.put(iHandler.getType(), iHandler.getThermodynamics());
						initializeNode(iHandler);
					}
					else if (subHandler instanceof MismatchHandler) {
						MismatchHandler mHandler = (MismatchHandler) subHandler;
						map.put(mHandler.getType(), mHandler.getThermodynamics());
						initializeNode(mHandler);
					}
					
				}
			} else {
				completedNode();
			}
			subHandler = null;
		}
}

package melting.handlers;

import java.util.HashMap;

import melting.Thermodynamics;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class DataHandler extends NodeHandler {
		private String type;
		private HashMap<String, Thermodynamics> map = new HashMap<String, Thermodynamics>();
		
		public DataHandler(HashMap<String, Thermodynamics> map){
			this.map = map;
		}
		
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
			if (subHandler == null) {
				if (name.equals("data")){
					this.setType(attributes.getValue("type"));
				}
				else {
					subHandler = new ThermoHandler(name);
					subHandler.startElement(uri, localName, name, attributes);
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
						ThermoHandler handler = (ThermoHandler) subHandler;
						String key = name;
						if (handler.getAttribut().containsKey("type")) {
							key += handler.getAttribut().get("type");
						}
						if (handler.getAttribut().containsKey("loop")) {
							key += "loop" + handler.getAttribut().get("loop");
						}
						if (handler.getAttribut().containsKey("size")) {
							key += "size" + handler.getAttribut().get("size");
						}
						if (handler.getAttribut().containsKey("sequence")) {
							key += handler.getAttribut().get("sequence");
						}
						if (handler.getAttribut().containsKey("closing")) {
							key += "close" + handler.getAttribut().get("closing");
						}
						if (handler.getAttribut().containsKey("sens")) {
							key += "sens" + handler.getAttribut().get("sens");
						}
						if (handler.getAttribut().containsKey("parameter")) {
							key += "parameter" + handler.getAttribut().get("parameter");
						}
						map.put(key, handler.getThermo());
						handler.initializeAttributes();	
					subHandler = null;
				}
			}
			else {
				completedNode();
			}
		}
}

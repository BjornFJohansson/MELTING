package melting;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import melting.exceptions.FileException;
import melting.handlers.DataHandler;

import org.xml.sax.SAXException;

public class FileReader {

	private static HashMap<String, HashMap<String, Thermodynamics>> loadedData = new HashMap<String, HashMap<String,Thermodynamics>>();
	
	public static HashMap<String, Thermodynamics> readFile(File file, HashMap<String, Thermodynamics> map) throws ParserConfigurationException,
			SAXException {
		if (loadedData.containsKey(file.getName())){
			return loadedData.get(file.getName());
		}
		else {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			try {
				DataHandler dataHandler = new DataHandler();
				saxParser.parse(file, dataHandler);
				loadedData.put(file.getName(), dataHandler.getMap());
				
				map.putAll(dataHandler.getMap());
				return map;
			} catch (IOException e) {
				throw new FileException("One of the file containing the thermodynamic parameters can't be parsed.", e);
			}
		}
	}

	public static HashMap<String, HashMap<String, Thermodynamics>> getLoadedData() {
		return loadedData;
	}
	
}

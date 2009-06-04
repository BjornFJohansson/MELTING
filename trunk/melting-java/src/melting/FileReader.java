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

	public HashMap<String, Thermodynamics> readFile(File file, HashMap<String, Thermodynamics> map) throws ParserConfigurationException,
			SAXException {
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		try {
			DataHandler dataHandler = new DataHandler(map);
			saxParser.parse(file, dataHandler);
			return dataHandler.getMap();
		} catch (IOException e) {
			throw new FileException("One of the file containing the thermodynamic parameters can't be parsed.");
		}
	}
	
}

package melting;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import melting.handlers.DataHandler;

import org.xml.sax.SAXException;

public class FileReader {

	public void readFile(File file, HashMap<String, Thermodynamics> map) throws ParserConfigurationException,
			SAXException {
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		try {
			saxParser.parse(file, new DataHandler(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
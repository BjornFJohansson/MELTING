package melting;
import handlers.DataHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class FileReader {
	
	public HashMap<String, Thermodynamics> map = new HashMap<String, Thermodynamics>();

	public void readFile(File file, HashMap<String, Thermodynamics> map) throws ParserConfigurationException,
			SAXException {
		this.map = map;
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		try {
			saxParser.parse(file, new DataHandler());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

package melting;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class MainTest {

	public static void main(String[] args) {
		HashMap<String, Thermodynamics> map = new HashMap<String, Thermodynamics>();
		File santalucia = new File("MELTING_Datas/Santalucia1996nn.xml");
		FileReader f = new FileReader();
		try {
			f.readFile(santalucia, map);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}

}

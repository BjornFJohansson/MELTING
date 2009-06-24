package melting;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class MeltingFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuffer message = new StringBuffer();
		
		if (record.getLevel().equals(Level.WARNING) || record.getLevel().equals(Level.SEVERE)){
			if (record.getThrown() != null) {
				message.append("EXCEPTION : ");
				message.append(record.getMessage());
				
				//record.getThrown().printStackTrace(message);
				message.append("\n");
			}
			message.append(record.getLevel() + " : " + record.getMessage() + "\n");
		}
		else {
			message.append(record.getMessage() + "\n");
		}
		return message.toString();
	}

}

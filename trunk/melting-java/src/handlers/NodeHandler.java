package handlers;
import org.xml.sax.helpers.DefaultHandler;

public abstract class NodeHandler extends DefaultHandler{
	
		private boolean completed = false;
		protected NodeHandler subHandler;
		
		public boolean hasCompletedNode() {
			return completed;
		}
		
		protected void completedNode() {
			completed = true;
		}
}

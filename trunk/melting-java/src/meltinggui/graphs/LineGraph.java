package meltinggui.graphs;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



/**
 * This class defines a line graph plot. Below the complete plotted values, a secondary plot 
 * representing a zooming window is also painted. This window is updated by moving the mouse 
 * on the former plot which will cause a sliding window movement indicating which plot area 
 * is currently zoomed.
 *
 * @author dallepep
 */
public class LineGraph extends JPanel {

	private static final long serialVersionUID = -7893883434501058128L;

	// Graphic variables
	/** A reference of the Graphics object used for plotting. */
	private Graphics g;	
	/** The window height. */
	private int height = -1;
	/** The window width. */
	private int width = -1;
	/** The length of a tick mark. */
	private int tickMarkLength = 3;
	/** The minimum value for the y axis. */
	private double minY;
	/** The maximum value for the y axis. */
	private double maxY;
	/** The interval for the y axis. */
	private double yInterval;
	/** The starting index of the data neighbourhood to plot ( [dataIdxStart, dataIdxEnd) ). */
	private int dataIdxStart = -1;
	/** The ending index of the data neighbourhood to plot ( [dataIdxStart, dataIdxEnd) ). */
	private int dataIdxEnd = -1;
	
	// data to plot
	/** An array containing the x axis values (numeric labels). */
	private String [] xCategories;
	/** An array containing the x axis values (bases labels). */
	private String [] xBases;
	/** An array containing the data points to plot. */
	private double [] data;	
	/** The graph title. */
	private String graphTitle;
	/** The x axis main label. */
	private String xLabel;
	/** The y axis main label. */
	private String yLabel;
	
	
	// sliding window management
	/** The sliding window object used to scroll and zoom the main plot. */
	private SlidingWindow slidingWindow = new SlidingWindow(15);
	/** The current rectangle representing the sliding window. */
	private Rectangle currRectangle = null;
	/** The mouse motion listener to play with the sliding window. This changes depending 
	 * on mouse movement (hovering), rather than mouse dragging.
	 */
	private MouseMotionListener mouseMotionListener = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) { }
		@Override
		public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            for(int j = 0; j < slidingWindow.getRectanglesSize(); j++) {
                Rectangle r = slidingWindow.getRectangle(j);
                if(r.contains(p)) {
                	currRectangle = r;
                	slidingWindow.setCurrentRectangleIndex(j);
                    break;
                }
            }
			repaint();
        }
	};
	
	
	
	
	
	// Constructors
	/** Constructor without bases. */
	public LineGraph (double[] data, double minY, double maxY, String xLabel, String yLabel, int[] xCategories, String graphTitle) {
		this(data, minY, maxY, xLabel, yLabel, new String[0], null, graphTitle);
		this.xCategories = new String [xCategories.length];
		for (int i=0;i<xCategories.length;i++) {
			this.xCategories[i] = ""+xCategories[i];
		}
	}
	
	/** Constructor including bases bases. */
	public LineGraph (double[] data, double minY, double maxY, String xLabel, String yLabel, String[] xCategories, String[] xBases, String graphTitle) {
		this.data = data;
		this.minY = minY;
		this.maxY = maxY;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.xCategories = xCategories;
		this.xBases = xBases;
		this.graphTitle = graphTitle;
		this.yInterval = new AxisScale (minY, maxY).getInterval();
		this.addMouseMotionListener(mouseMotionListener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getPreferredSize () {
		return new Dimension(800,600);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Dimension getMinimumSize () {
		return new Dimension(300,200);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint (Graphics g) {
		super.paint(g);		
		this.g = g;
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		if (g instanceof Graphics2D) {
			((Graphics2D)g).setStroke(new BasicStroke(1));
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}		
		
		double yStartPos = 40;
		double yOffset = getHeight()/2 - 2*yStartPos;	
		// paint the first plot containing the sliding window
		plotLineGraph(true, yStartPos, yOffset);
				
		// Now add the sliding window and the second plot
        if(currRectangle != null) {
        	// paint the sliding window
			if (g instanceof Graphics2D) {
				((Graphics2D)g).setStroke(new BasicStroke(1.5f));
			}
			g.setColor(Color.RED);
			g.drawRect((int)currRectangle.getX(), (int)currRectangle.getY(), (int)currRectangle.getWidth(), (int)currRectangle.getHeight());
			g.setColor(Color.BLACK);
			if (g instanceof Graphics2D) {
				((Graphics2D)g).setStroke(new BasicStroke(1));
			}

			// paint the second plot (zoom plot)
			yStartPos = getHeight()/2;
			
			plotLineGraph(false, yStartPos, yOffset);
        }

	}
	
	
	/**
	 * Returns a consistent vertical value for the object position within the plot.
	 * @param y
	 * @param yStartPos
	 * @param yOffset
	 * @return the y coordinate
	 */
	private int getY(double y, double yStartPos, double yOffset) {
		return (int) (yStartPos + yOffset - ((yOffset)/(maxY-minY))*(y-minY));
	}
	
	
	/**
	 * This method creates the rectangles reproducing the sliding window. It is also used to 
	 * update the rectangles sizes every time the window size changes.
	 * @param baseWidth the size of one step of x axis
	 * @param xOffset the offset from the left border
	 * @param yStart the starting point for the y axis (it is relative within the plot)
	 * @param yOffset the vertical offset from yStartPos
	 * @param yStartPos the starting position of the plot (it is relative to the two plots)
	 */
	private void updateSlidingWindow(double baseWidth, double xOffset, double yStart, double yOffset, double yStartPos) {
		if(width != getWidth() || height != getHeight()) {
			slidingWindow.reset();
			int slidingWindowSize = slidingWindow.windowSize();
			Rectangle r;
			for(int i=0; i<data.length-slidingWindowSize+1; i++) {
				r = new Rectangle((int)(xOffset+i*baseWidth), (int)(yStartPos), (int)(baseWidth*slidingWindowSize), getY(yStart-yInterval, yStartPos, yOffset)-(int)(yStartPos));
				slidingWindow.addRectangle(r);		
			}
			// Just process this one time.
			if(slidingWindow.isUnset()) {
				// Set the first sliding window at the initial position (first rectangle)
				currRectangle = slidingWindow.getRectangle(0);
            	slidingWindow.setCurrentRectangleIndex(0);
			} else {
				// Update the current rectangle coordinates so that it will be refreshed correctly.
				currRectangle = slidingWindow.getRectangle(slidingWindow.getCurrentRectangleIndex());
			}
			// update the window width and height
			width = getWidth();
			height = getHeight();
		}
	}

	
	/**
	 * Paints the line graph. This method is used to paint both the line plots
	 * @param isSlidingWindowContainer true if the plot containing the sliding window is being plotted.
	 * @param yStartPos the vertical starting position of the plot
	 * @param yOffset the vertical offset from yStartPos
	 */
	private void plotLineGraph(boolean isSlidingWindowContainer, double yStartPos, double yOffset) {
		Font normalFont = g.getFont();
		Font smallFont = normalFont.deriveFont(normalFont.getSize() * 0.8f);

		
		int lastY = 0;
		double yStart;
		if (minY % yInterval == 0) {
			yStart = minY;
		}
		else {
			yStart = yInterval * (((int)minY/yInterval)+1);
		}
		int xOffset = 0;

		
		// Draw the labels for the yAxis
		g.setFont(smallFont);
		int lastYLabelEnd = Integer.MAX_VALUE;
		for (double i=yStart-yInterval;i<=maxY;i+=yInterval) {
			// previous code
			//String label = scale.format(currentValue);
			String label = "" + new BigDecimal(i).setScale(
					AxisScale.getFirstSignificantDecimalPosition(yInterval), RoundingMode.HALF_UP).doubleValue();	
			label = label.replaceAll(".0$", ""); // Don't leave trailing .0s where we don't need them.			
		
			// Calculate the new xOffset depending on the widest ylabel.
			int width = g.getFontMetrics().stringWidth(label);
			if (width > xOffset) {
				xOffset = width;
			}
			// place the y axis labels so that they don't overlap when the plot is resized.
			int baseNumberHeight = g.getFontMetrics().getHeight();
			int baseNumberPosition = (int)(getY(i, yStartPos, yOffset)+(baseNumberHeight/2));
			if (baseNumberPosition + baseNumberHeight < lastYLabelEnd) {
				// Draw the y axis labels
				g.drawString(label, 23, getY(i, yStartPos, yOffset)+(baseNumberHeight/2));
				lastYLabelEnd = baseNumberPosition + 2;
			}
		}
		g.setFont(normalFont);
		
	
		// Give the x axis a bit of breathing space
		xOffset += 30;
			

		
		
		// Now draw horizontal lines across from the y axis
		for (double i=yStart-yInterval;i<=maxY;i+=yInterval) {
			// Draw horizontal line
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(xOffset, getY(i, yStartPos, yOffset), getWidth()-10, getY(i, yStartPos, yOffset));
			// Draw tick marks on the y axis
			g.setColor(Color.BLACK);
			g.drawLine(xOffset-tickMarkLength, getY(i, yStartPos, yOffset), xOffset, getY(i, yStartPos, yOffset));
		}

		
		
		// Set the indexes
		dataIdxStart = 0;
		dataIdxEnd = data.length;
		// Set the base width
		double baseWidth = 1.0*(getWidth()-(xOffset+10))/dataIdxEnd;
		// System.out.println("Base Width is "+baseWidth);
		
			
		
		// Now update the sliding window (rectangles + data if required).
		if(isSlidingWindowContainer) {
			// Draw the graph title
			int titleWidth = g.getFontMetrics().stringWidth(graphTitle);
			g.drawString(graphTitle, (xOffset + ((getWidth()-(xOffset+10))/2)) - (titleWidth/2), 30);
			updateSlidingWindow(baseWidth, xOffset, yStart, yOffset, yStartPos);
		} else {
			// Adjust the indexes
			dataIdxStart = slidingWindow.getCurrentRectangleIndex();
			dataIdxEnd = dataIdxStart + slidingWindow.windowSize();
			baseWidth = 1.0*(getWidth()-(xOffset+10))/slidingWindow.windowSize();	
			//System.out.println(dataIdxStart + " " + dataIdxEnd);
		}
		
		// Draw the labels for the x axis (STANDARD HORIZONTAL VISUALISATION)
		int lastXLabelEnd = 0;
		g.setFont(smallFont);
		for (int i=dataIdxStart; i<dataIdxEnd; i++) {
			String baseNumber = ""+xCategories[i];
			int baseNumberWidth = g.getFontMetrics().stringWidth(baseNumber);
			int baseNumberPosition = (int)((baseWidth/2)+xOffset+(baseWidth*(i-dataIdxStart))-(baseNumberWidth/2));
			int baseNamePosition = (int)((baseWidth/2)+xOffset+(baseWidth*(i-dataIdxStart))-(g.getFontMetrics().stringWidth("A")/2));
			if (baseNumberPosition > lastXLabelEnd) {
				// Draw the x axis labels
				g.drawString(baseNumber,baseNumberPosition, getY(yStart-yInterval, yStartPos, yOffset)+15);
				// Draw the tick marks
				g.drawLine(baseNamePosition+3, getY(yStart-yInterval, yStartPos, yOffset), baseNamePosition+3, getY(yStart-yInterval, yStartPos, yOffset)+tickMarkLength);
				if(!isSlidingWindowContainer) {
					g.drawString(xBases[i],baseNamePosition, getY(yStart-yInterval, yStartPos, yOffset)+30);
				}
				lastXLabelEnd = baseNumberPosition+baseNumberWidth+5;
			}
		}
		g.setFont(normalFont);
		
		
		// Now draw the data set
		if (g instanceof Graphics2D) {
			((Graphics2D)g).setStroke(new BasicStroke(1));
		}
		g.setColor(Color.BLUE);
		lastY = getY(data[dataIdxStart], yStartPos, yOffset);
		for (int i=dataIdxStart+1; i<dataIdxEnd; i++) {
			if (Double.isNaN(data[i])) break;
			int thisY = getY(data[i], yStartPos, yOffset);
			g.drawLine((int)((baseWidth/2)+xOffset+(baseWidth*(i-dataIdxStart-1))), lastY, (int)((baseWidth/2)+xOffset+(baseWidth*(i-dataIdxStart))), thisY);
			lastY = thisY;
		}
		g.setColor(Color.BLACK);

		
		
		// Now draw the x, y axes
		// Draw the x axis
		g.drawLine(xOffset, getY(yStart-yInterval, yStartPos, yOffset), getWidth()-10, getY(yStart-yInterval, yStartPos, yOffset));
		// Draw the y axis
		g.drawLine(xOffset, getY(yStart-yInterval, yStartPos, yOffset), xOffset, (int)(yStartPos));
		
		
		
		// Draw the xLabel under the xAxis
		if(isSlidingWindowContainer) {
			g.drawString(xLabel, (getWidth()/2) - (g.getFontMetrics().stringWidth(xLabel)/2), getY(yStart-yInterval, yStartPos, yOffset)+35);
		} else {
			g.drawString(xLabel, (getWidth()/2) - (g.getFontMetrics().stringWidth(xLabel)/2), getY(yStart-yInterval, yStartPos, yOffset)+50);
		}
		
		
		// Draw the yLabel on the left of the yAxis
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D)g;
			AffineTransform orig = g2.getTransform();
			g2.rotate(-Math.PI/2);
			g2.setColor(Color.BLACK);
			g2.drawString(yLabel, ((int)(-yStartPos)-getY(yStart-yInterval, yStartPos, yOffset))/2 - (g.getFontMetrics().stringWidth(yLabel)/2), 12);
			g2.setTransform(orig);
		}

	}


	/**
	 * A simple main as use case.
	 * @param argv
	 */
	public static void main(String[] argv) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				int size = 5000;
				double yMin=Integer.MAX_VALUE, yMax=0;
				double[] data = new double[size];
				String[] categories = new String[size];
				String[] bases = new String[size];
				Random r = new Random(), s = new Random();
			
				for(int i=0; i<size; i++) {
					data[i] = r.nextGaussian()*5 + 70;
					if(yMin>data[i]) {
						yMin = data[i];
					} else if(yMax<data[i]) {
						yMax = data[i];
					}
					switch(s.nextInt(4)) {
					case 0: bases[i]="A"; break;
					case 1: bases[i]="C"; break;
					case 2: bases[i]="G"; break;
					case 3: bases[i]="T"; break;
					}
					categories[i] = ""+(i+1);
				}
		
				JFrame f = new JFrame();
				f.setSize(600, 550);
				f.getContentPane().add(new LineGraph(data, yMin-2, yMax+6, "x axis", "y axis", categories, bases, "Graph Title"));
		
				WindowListener wndCloser = new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
				    System.exit(0);
				   }
				};
			   f.addWindowListener(wndCloser);
			   f.setVisible(true);
			}
		});
	}
	
}

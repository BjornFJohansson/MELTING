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



public class LineGraph extends JPanel {

	private static final long serialVersionUID = -7893883434501058128L;
	private String xLabel;
	private String yLabel;
	private String [] xCategories;
	private String [] xBases;
	private boolean plotBases = true;
	private double [] data;
	private String graphTitle;
	private double minY;
	private double maxY;
	private double yInterval;
	private int height = -1;
	private int width = -1;
	
	Graphics g;	
	
	// sliding window management
	SlidingWindow slidingWindow = new SlidingWindow(15);
	private Rectangle currRectangle = null;
	private double[] currData = null;
	private String[] currCategories = null;
	private String[] currBases = null;
	

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
                	currData = slidingWindow.getData(j);
                	currCategories = slidingWindow.getCategories(j);
                	if(plotBases) {
                		currBases = slidingWindow.getBases(j);
                	}
                    break;
                }
            }
			repaint();
        }
	};
	
	
	
	
	
	
	public LineGraph (double[] data, double minY, double maxY, String xLabel, String yLabel, int[] xCategories, String graphTitle) {
		this(data, minY, maxY, xLabel, yLabel, new String[0], null, graphTitle);
		this.xCategories = new String [xCategories.length];
		for (int i=0;i<xCategories.length;i++) {
			this.xCategories[i] = ""+xCategories[i];
		}
	}
	
	public LineGraph (double[] data, double minY, double maxY, String xLabel, String yLabel, String[] xCategories, String[] xBases, String graphTitle) {
		this.data = data;
		this.minY = minY;
		this.maxY = maxY;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.xCategories = xCategories;
		this.xBases = xBases;
		if(xBases == null) {
			plotBases = false;
		}
		this.graphTitle = graphTitle;
		this.yInterval = new AxisScale (minY, maxY).getInterval();
		this.addMouseMotionListener(mouseMotionListener);
	}
	
	@Override
	public Dimension getPreferredSize () {
		return new Dimension(800,600);
	}

	@Override
	public Dimension getMinimumSize () {
		return new Dimension(100,200);
	}
	
	
	
	
	
	// TODO THIS CAN BE HEAVELY OPTIMISED
	// TODO No need to replicate the info in data, xCategories, xBases. 
	// Just keep track of the first index (which is the rectangle index). To this you extract the values between [RectIndex, slidingWindowSize).
	// Doing so, you save a lot of memory and a bit of computation when the plot is created at start.
	private void updateSlidingWindow(double baseWidth, double xOffset, double yStart, double yOffset, double yStartPos) {
		if(width != getWidth() || height != getHeight()) {
			slidingWindow.resetRectangles();
			int slidingWindowSize = slidingWindow.slidingWindowSize();
			Rectangle r;
			for(int i=0; i<data.length-slidingWindowSize+1; i++) {
				r = new Rectangle((int)(xOffset+i*baseWidth), (int)(yStartPos), (int)(baseWidth*slidingWindowSize), getY(yStart-yInterval, yStartPos, yOffset)-(int)(yStartPos));
				slidingWindow.addRectangle(r);		
			}
			// Just process this one time.
			if(!slidingWindow.containsData()) {
				for(int i=0; i<data.length-slidingWindowSize+1; i++) {
					double[] d = new double[slidingWindowSize];
					String[] c = new String[slidingWindowSize];
					String[] b = new String[slidingWindowSize];
					for(int j=0; j < slidingWindowSize; j++) {
						d[j] = data[i+j];
						c[j] = xCategories[i+j];
						b[j] = xBases[i+j];
					}
					slidingWindow.addData(d);
					slidingWindow.addCategories(c);
					if(plotBases) {
						slidingWindow.addBases(b);
					}
				}
				// Set the first sliding window at the initial position (first rectangle)
				currRectangle = slidingWindow.getRectangle(0);
				currData = slidingWindow.getData(0);
				currCategories = slidingWindow.getCategories(0);
				if(plotBases) {
					currBases = slidingWindow.getBases(0);
				}
			}
			
			// Now update the current rectangle coordinates so that it will be refreshed correctly.
			currRectangle = slidingWindow.getRectangle(slidingWindow.getCurrentIndex());
			// update the window width and height
			width = getWidth();
			height = getHeight();
		}
	}
	
	
	
	
	
	@Override
	public void paint (Graphics g) {
		super.paint(g);		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		
		double yStartPos = 40;
		double yOffset = getHeight()/3;
		
		if (g instanceof Graphics2D) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}		
		
		this.g = g;
		
		plotLineGraph(true, data, xCategories, xBases, yStartPos, yOffset);
		
		// add the sliding window
        if(currRectangle != null) {
			g.setColor(Color.RED);
			g.drawRect((int)currRectangle.getX(), (int)currRectangle.getY(), (int)currRectangle.getWidth(), (int)currRectangle.getHeight());
			g.setColor(Color.BLACK);

			// plot the second plot
			yStartPos = yStartPos + yOffset + 70;
			plotLineGraph(false, currData, currCategories, currBases, yStartPos, yOffset);	
        }

	}
	
	
	
	
	
	
	private int getY(double y, double yStartPos, double yOffset) {
		return (int) (yStartPos+yOffset - ((yOffset)/(maxY-minY))*(y-minY));		
	}
	
	
	private void plotLineGraph(boolean isSlidingWindowContainer, double[] dataToPlot, String[] xCategoriesToPlot, String[] xBasesToPlot, double yStartPos, double yOffset) {
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
		
		
		
		// TODO add a gap between numbers so that they don't overlap with each other
		// Draw the labels for the yAxis
		g.setFont(smallFont);
		for (double i=yStart-yInterval;i<=maxY;i+=yInterval) {
			//String label = scale.format(currentValue);
			String label = "" + new BigDecimal(i).setScale(
					AxisScale.getFirstSignificantDecimalPosition(yInterval), RoundingMode.HALF_UP).doubleValue();	
			label = label.replaceAll(".0$", ""); // Don't leave trailing .0s where we don't need them.			
			
			int width = g.getFontMetrics().stringWidth(label);
			if (width > xOffset) {
				xOffset = width;
			}
			g.drawString(label, 25, getY(i, yStartPos, yOffset)+(g.getFontMetrics().getAscent()/2));			
		}
		g.setFont(normalFont);
		
	
		// Give the x axis a bit of breathing space
		xOffset += 30;
			

		
		
		// Now draw horizontal lines across from the y axis
		g.setColor(new Color(180,180,180));
		for (double i=yStart;i<=maxY;i+=yInterval) {
			g.drawLine(xOffset, getY(i, yStartPos, yOffset), getWidth()-10, getY(i, yStartPos, yOffset));
		}
		g.setColor(Color.BLACK);
		
		
		
		
		// Set the base width
		double baseWidth = 1.0*(getWidth()-(xOffset+10))/dataToPlot.length;
		// System.out.println("Base Width is "+baseWidth);
		
		
		
		// Now update the sliding window (rectangles + data if required).
		if(isSlidingWindowContainer) {
			// Draw the graph title
			int titleWidth = g.getFontMetrics().stringWidth(graphTitle);
			g.drawString(graphTitle, (xOffset + ((getWidth()-(xOffset+10))/2)) - (titleWidth/2), 30);
			updateSlidingWindow(baseWidth, xOffset, yStart, yOffset, yStartPos);
		}
		
		
		
		
		// Draw the labels for the x axis
		int lastXLabelEnd = 0;
		g.setFont(smallFont);
		for (int i=0;i<dataToPlot.length;i++) {
			String baseNumber = ""+xCategoriesToPlot[i];
			int baseNumberWidth = g.getFontMetrics().stringWidth(baseNumber);
			int baseNumberPosition = (int)((baseWidth/2)+xOffset+(baseWidth*i)-(baseNumberWidth/2));
			int baseNamePosition = (int)((baseWidth/2)+xOffset+(baseWidth*i)-(g.getFontMetrics().stringWidth("A")/2));
			if (baseNumberPosition > lastXLabelEnd) {
				// Draw the x axis labels
				g.drawString(baseNumber,baseNumberPosition, getY(yStart-yInterval, yStartPos, yOffset)+15);
				// Draw the ticks
				g.drawLine(baseNamePosition+3, getY(yStart-yInterval, yStartPos, yOffset), baseNamePosition+3, getY(yStart-yInterval, yStartPos, yOffset)+4);
				if(!isSlidingWindowContainer) {
					g.drawString(xBasesToPlot[i],baseNamePosition, getY(yStart-yInterval, yStartPos, yOffset)+30);
				}
				lastXLabelEnd = baseNumberPosition+baseNumberWidth+5;
			}
		}
		g.setFont(normalFont);
		
		
		
		
		// Now draw the data set
		if (g instanceof Graphics2D) {
			((Graphics2D)g).setStroke(new BasicStroke(2));
		}
		g.setColor(Color.BLUE);
		lastY = getY(dataToPlot[0], yStartPos, yOffset);
		for (int i=1;i<dataToPlot.length;i++) {
			if (Double.isNaN(dataToPlot[i])) break;
			int thisY = getY(dataToPlot[i], yStartPos, yOffset);
			g.drawLine((int)((baseWidth/2)+xOffset+(baseWidth*(i-1))), lastY, (int)((baseWidth/2)+xOffset+(baseWidth*i)), thisY);
			lastY = thisY;
		}
		

		
		
		// Now draw the x, y axes
		if (g instanceof Graphics2D) {
			((Graphics2D)g).setStroke(new BasicStroke(1));
		}
		g.setColor(Color.BLACK);
		// Draw the x axis
		g.drawLine(xOffset, getY(yStart-yInterval, yStartPos, yOffset), getWidth()-10, getY(yStart-yInterval, yStartPos, yOffset));
		// Draw the y axis
		g.drawLine(xOffset, getY(yStart-yInterval, yStartPos, yOffset), xOffset, (int)(yStartPos));
		
		
		
		// Draw the xLabel under the xAxis
		if(isSlidingWindowContainer) {
			g.drawString(xLabel, (getWidth()/2) - (g.getFontMetrics().stringWidth(xLabel)/2), getY(yStart-yInterval, yStartPos, yOffset)+40);
		} else {
			g.drawString(xLabel, (getWidth()/2) - (g.getFontMetrics().stringWidth(xLabel)/2), getY(yStart-yInterval, yStartPos, yOffset)+55);
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


	
	public static void main(String[] argv) {

		int size = 500;
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
		f.setSize(800, 600);
		f.getContentPane().add(new LineGraph(data, yMin-2, yMax+6, "this is the x axis", "this is the y axis", categories, bases, "Graph Title"));

		WindowListener wndCloser = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
		    System.exit(0);
		   }
		};
	   f.addWindowListener(wndCloser);
	   f.setVisible(true);
	}
	
}

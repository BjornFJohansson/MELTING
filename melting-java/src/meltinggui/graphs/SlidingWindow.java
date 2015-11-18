package meltinggui.graphs;

import java.awt.Rectangle;
import java.util.ArrayList;


/**
 * A simple sliding window representation using awt rectangles.
 * 
 * @author dallepep
 */
public class SlidingWindow {
	
	/**
	 * A list of rectangles representing the sliding window.
	 */
	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	/**
	 * The current selected rectangle index.
	 */
	private int currentRectangleIndex = -1;
	/**
	 * The number of elements that each rectangle represents.
	 */
	private int size = 10;
	
	/** 
	 * Default constructor.
	 */
	public SlidingWindow() {}
	
	/**
	 * Constructor with a sliding window size.
	 * @param size
	 */
	public SlidingWindow(int size) {
		this.size = size;
	}
	
	/** 
	 * Resets the sliding window.
	 */
	public void reset() {
		rectangles = new ArrayList<Rectangle>();
	}

	/**
	 * Adds a rectangle to the sliding window.
	 * @param r
	 */
	public void addRectangle(Rectangle r) {
		rectangles.add(r);
	}
	
	/**
	 * Returns the i-th rectangle.
	 * @param i
	 * @return the i-th rectangle.
	 */
	public Rectangle getRectangle(int i) {
		if(i >= 0 && i < rectangles.size()) {
			return rectangles.get(i);
		} 
		return null;
	}
	
	/**
	 * Returns the index for the current rectangle.
	 * @return the index for the current rectangle.
	 */
	public int getCurrentRectangleIndex() { 
		return currentRectangleIndex;
	}
	
	/**
	 * Sets the index for the current the current rectangle.
	 */
	public void setCurrentRectangleIndex(int currentRectangleIndex) { 
		this.currentRectangleIndex = currentRectangleIndex;
	}
	
	/**
	 * Returns true if the index for the current rectangle is not set.
	 * @return true if the index for the current rectangle is not set.
	 */
	public boolean isUnset() {
		return currentRectangleIndex == -1;
	}
		
	/**
	 * Returns the number of rectangles.
	 * @return the number of rectangles.
	 */
	public int getRectanglesSize() {
		return rectangles.size();
	}
	
	/**
	 * Returns the number of elements each rectangle represents (the size of the sliding window).
	 * @return the size of the sliding window.
	 */
	public int windowSize() {
		return size;
	}
	
}

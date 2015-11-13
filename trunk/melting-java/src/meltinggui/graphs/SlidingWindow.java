package meltinggui.graphs;

import java.awt.Rectangle;
import java.util.ArrayList;

public class SlidingWindow {
	
	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	private ArrayList<double[]> data = new ArrayList<double[]>();
	private ArrayList<String[]> categories = new ArrayList<String[]>();
	private ArrayList<String[]> bases = new ArrayList<String[]>();
	private int currentIndex = -1;
	private int size = 10;
	
	public SlidingWindow() {}
	public SlidingWindow(int size) {
		this.size = size;
	}
	
	public void resetRectangles() {
		rectangles = new ArrayList<Rectangle>();
	}
	
	public void reset() {
		rectangles = new ArrayList<Rectangle>();
		data = new ArrayList<double[]>();
		categories = new ArrayList<String[]>();
		bases = new ArrayList<String[]>();
	}
	
	public void addRectangle(Rectangle r) {
		rectangles.add(r);
	}
	
	public void addData(double[] d) {
		data.add(d);
	}
	
	public void addCategories(String[] s) {
		categories.add(s);
	}
	
	public void addBases(String[] s) {
		bases.add(s);
	}
	
	public Rectangle getRectangle(int i) {
		if(i >= 0 && i < rectangles.size()) {
			return rectangles.get(i);
		} 
		return null;
	}
	
	public double[] getData(int i) {
		if(i >= 0 && i < data.size()) {
			currentIndex = i;
			return data.get(i);
		}
		return null;
	}
	
	public boolean containsData() {
		return !data.isEmpty();
	}
	
	public String[] getCategories(int i) {
		if(i >= 0 && i < categories.size()) {
			return categories.get(i);
		}
		return null;
	}
	
	public String[] getBases(int i) {
		if(i >= 0 && i < bases.size()) {
			return bases.get(i);
		} 
		return null;
	}
	
	public int getCurrentIndex() { 
		return currentIndex;
	}
	
	public int getRectanglesSize() {
		return rectangles.size();
	}
	
	public int slidingWindowSize() {
		return size;
	}
	
}

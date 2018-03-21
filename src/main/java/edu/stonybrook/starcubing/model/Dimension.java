package edu.stonybrook.starcubing.model;

import java.util.ArrayList;
import java.util.List;

public class Dimension {

	public List<String> values = new ArrayList<String>();
	char label;
	public int cardinality;
	public Dimension(char label) {
		super();
		this.label = label;
	}

	public char getLowerCLabel(){
		return Character.toLowerCase(label);
	}
	
	public char getUpperCLabel(){
		return label;
	}
	
}

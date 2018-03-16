package edu.stonybrook.starcubing.model;

import java.util.HashMap;
import java.util.Map;

public class Cuboid {

	public String attrValue;
	public int aggrVal;
	public Map<String, Cuboid> children;
	
	public Cuboid(){
		children = new HashMap<String, Cuboid>();
		aggrVal = 0;
		attrValue = "~";
	}
	
	public Cuboid(String attrValue){
		children = new HashMap<String, Cuboid>();
		this.attrValue = attrValue;
		aggrVal = 0;
	}
	
	public void addChild(Cuboid child){
		children.put(child.attrValue, child);
	}
	
	@Override
	public boolean equals(Object o){
		if(null == o) return false;
		if(!Cuboid.class.isAssignableFrom(o.getClass())) return false;
		Cuboid co = (Cuboid) o;
		if (!co.attrValue.equals(this.attrValue)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		return attrValue.hashCode();
	}
}

package edu.stonybrook.starcubing.model;

import java.util.ArrayList;
import java.util.List;

public class TableNode {

	List<String> tuple = new ArrayList<String>();
	
	@Override
	public boolean equals(Object o) {
		if(null == o) return false;
		if(!TableNode.class.isAssignableFrom(o.getClass())) return false;
		TableNode co = (TableNode) o;
		//if (!co.attrValue.equals(this.attrValue)) return false;
		for (int i = 0; i < this.tuple.size(); i++) {
			if(checkAttrMatch(co, i))
				continue;
			else 
				return false;
		}
		return true;
	}

	private boolean checkAttrMatch(TableNode co, int i) {
		return tuple.get(i).equals("*") || co.tuple.get(i).equals("*") || tuple.get(i).equals(co.tuple.get(i));
	}
}

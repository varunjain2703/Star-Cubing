package edu.stonybrook.starcubing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.stonybrook.starcubing.model.Cuboid;
import edu.stonybrook.starcubing.util.Util;

public class Algorithm {
	
	public static Map<String, Integer> compressedBaseTable(List<List<String>> strList) {
		return Util.getOoccurrenceCount(getTuples(strList));
	}
	
	public static void replaceDimensions(List<List<String>> strList) {
		for (List<String> attribute : strList) {
			if (!attribute.isEmpty()) {
				Algorithm.dimensionIceBCheck(attribute, 2);
			}
		}
	}
	
	public static Cuboid cuboidTree(Map<String, Integer> baseTable) {
		Cuboid root = new Cuboid();
		for (Map.Entry<String, Integer> entry : baseTable.entrySet()) {
			String[] attrs = entry.getKey().split(",");
			Cuboid lastChild = null, parent = root;
			for (String attr : attrs) {
				parent.aggrVal += entry.getValue();
				if (!parent.children.containsKey(attr)) {
					parent.addChild(new Cuboid(attr));
				}
				parent = parent.children.get(attr);
				lastChild = parent;
			}
			if(null != lastChild)
				lastChild.aggrVal += entry.getValue();
		}
		return root;
	}
	
	private static <T> List<String> getTuples(List<List<T>> list) {
		List<String> concaList = new ArrayList<String>();
		for (int i = 0; i < list.get(5).size(); i++) {
			StringBuilder str = new StringBuilder();
			str.append(list.get(2).get(i));
			for (int j = 3; j < list.size(); j++) {
				str.append("," + list.get(j).get(i));
			}
			concaList.add(str.toString());
		}
		return concaList;
	}

	private static void dimensionIceBCheck(List<String> attribute, double iBC) {
		Map<String, Integer> countMap = Util.getOoccurrenceCount(attribute);
		for (int i = 0; i < attribute.size(); i++) {
			if (countMap.get(attribute.get(i)) < iBC)
				//attribute.set(i, -1.0);
				attribute.set(i, "*");
		}
	}
}

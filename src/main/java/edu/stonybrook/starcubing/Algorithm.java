package edu.stonybrook.starcubing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.stonybrook.starcubing.model.Cuboid;
import edu.stonybrook.starcubing.model.Dimension;
import edu.stonybrook.starcubing.util.Util;

public class Algorithm {

	static double iBC = 2;
	static double min_sup = 2;
	
	public static void setIBC(double iBC){
		Algorithm.iBC = iBC;
	}
	
	public static void setMinSup(double min_sup){
		Algorithm.min_sup = min_sup;
	}

	public static void sortDimensions(List<Dimension> list) {
		Collections.sort(list, new Comparator<Dimension>(){

			@Override
			public int compare(Dimension o1, Dimension o2) {
				return o2.cardinality - o1.cardinality;
			}
			
		});
	}
	
	public static Map<String, Integer> compressedBaseTable(List<Dimension> list) {
		return Util.getOoccurrenceCount(getTuples(list));
	}

	public static void replaceDimensions(List<Dimension> list) {
		for (Dimension attribute : list) {
			if (!attribute.values.isEmpty()) {
				Algorithm.dimensionIceBCheck(attribute);
			}
		}
	}

	public static Cuboid cuboidTree(Map<String, Integer> baseTable) {
		Cuboid root = new Cuboid();
		for (Map.Entry<String, Integer> entry : baseTable.entrySet()) {
			String[] attrs = entry.getKey().split(",");
			Cuboid lastChild = null,
					parent = root;
			for (String attr : attrs) {
				parent.aggrVal += entry.getValue();
				if (!parent.children.containsKey(attr)) {
					parent.addChild(new Cuboid(attr));
				}
				parent = parent.children.get(attr);
				lastChild = parent;
			}
			if (null != lastChild)
				lastChild.aggrVal += entry.getValue();
		}
		return root;
	}

	public static Map<String, Cuboid> starCubing(Cuboid root, int attributes) {
		Map<String, Cuboid> map = new HashMap<String, Cuboid>();
		StringBuilder sb = new StringBuilder();
		char attrRepresent = 'B';
		for (int i = 0; i < attributes; i++) {
			sb.append(attrRepresent);
			sb.append(',');
			attrRepresent++;
		}
		sb.append('/');
		for (Cuboid cuboid : root.children.values()) {
			starCubing(cuboid, map, sb.toString(), 0, new ArrayList<Cuboid>());
		}
		childPruning(map);
		return map;
	}
	
	private static void childPruning(Map<String, Cuboid> map) {
		Iterator<Map.Entry<String, Cuboid>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Cuboid> entry = iterator.next();
			if (entry.getValue().aggrVal < min_sup) {
				iterator.remove();
				continue;
			}
			boolean prune = true;
			for (String string : entry.getKey().substring(0, entry.getKey().indexOf("/")-1).split(",")) {
				if (!string.contains("*")/* && !string.equals("/")*/) {
					prune = false;
					break;
				}
			}
			if (prune)
				iterator.remove();
		}
	}

	private static void starCubing(Cuboid cNode, Map<String, Cuboid> map, String curTree, int level, List<Cuboid> addToList) {
		Cuboid levelCube = increaseAggrVal(cNode, map, curTree);
		String nextCurTree = nextTreeName(curTree, level, cNode.attrValue);
		for (Cuboid cuboid : cNode.children.values()) {
			List<Cuboid> nextAddToList = new ArrayList<Cuboid>();
			Cuboid c = increaseAggrVal(cuboid, levelCube.children, cuboid.attrValue);
			nextAddToList.add(c);
			for (Cuboid prevCub : addToList) {
				c = increaseAggrVal(cuboid, prevCub.children, cuboid.attrValue);
				nextAddToList.add(c);
			}
			starCubing(cuboid, map, nextCurTree, level + 1, nextAddToList);
		}
	}

	private static <T> Cuboid increaseAggrVal(Cuboid node, Map<String, Cuboid> map, String curTree) {
		Cuboid levelCube = null;
		if (map.containsKey(curTree)) {
			levelCube = map.get(curTree);
		} else {
			levelCube = new Cuboid(curTree);
			map.put(levelCube.attrValue, levelCube);
		}
		levelCube.aggrVal += node.aggrVal;
		return levelCube;
	}

	private static String nextTreeName(String treeName, int level, String cur) {
		String[] str = treeName.split(",");
		str[level] = cur;
		StringBuilder sb = new StringBuilder();
		for (String string : str) {
			sb.append(string);
			sb.append(",");
		}
		sb.append(cur);
		return sb.toString();
	}

	private static <T> List<String> getTuples(List<Dimension> list) {
		List<String> concaList = new ArrayList<String>();
		for (int i = 0; i < list.get(0).values.size(); i++) {
			StringBuilder str = new StringBuilder();
			str.append(list.get(0).values.get(i));
			for (int j = 1; j < list.size(); j++) {
				str.append("," + list.get(j).values.get(i));
			}
			concaList.add(str.toString());
		}
		return concaList;
	}

	private static void dimensionIceBCheck(Dimension attribute) {
		Map<String, Integer> countMap = Util.getOoccurrenceCount(attribute.values);
		attribute.cardinality = countMap.size();
		for (int i = 0; i < attribute.values.size(); i++) {
			if (countMap.get(attribute.values.get(i)) < iBC) {
				String str = attribute.getLowerCLabel() + "*";
				attribute.values.set(i, str);
			}
		}
	}
}

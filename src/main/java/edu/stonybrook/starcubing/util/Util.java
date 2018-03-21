package edu.stonybrook.starcubing.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.stonybrook.starcubing.model.Cuboid;
import edu.stonybrook.starcubing.model.Dimension;

public class Util {

	public static <T> Map<T, Integer> getOoccurrenceCount(List<T> attribute) {
		Map<T, Integer> countMap = new HashMap<T, Integer>();
		for (T value : attribute) {
			if (countMap.containsKey(value))
				countMap.put(value, (countMap.get(value)) + 1);
			else
				countMap.put(value, 1);
		}
		return countMap;
	}
	
	public static List<Dimension> readCSV(String path) {
		try {
			Scanner in = new Scanner(new File(path));
			String str = in.next();
			int cols = str.split(";").length;
			List<Dimension> list = new ArrayList<Dimension>();
			List<Set<String>> countCardinality =  new ArrayList<>();
			char label = 'A';
			for (int i = 0; i < cols; i++) {
				list.add(new Dimension(label++));
				countCardinality.add(new HashSet<>());
			}
			while (in.hasNext()){
				String[] strAttrs = in.next().split(";");
				for (int i = 0; i < strAttrs.length; i++) {
					if (!strAttrs[i].isEmpty()){
						list.get(i).values.add(strAttrs[i]);
						countCardinality.get(i).add(strAttrs[i]);
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				list.get(i).cardinality = countCardinality.get(i).size();
			}
			in.close();
			return list;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static List<Dimension> readXLSX(String path) {
		try {
			FileInputStream excelFile = new FileInputStream(new File(path));
			
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Cell cell;
			
			int cols = sheet.getRow(1).getPhysicalNumberOfCells(); // No of columns
			List<Dimension> list = new ArrayList<Dimension>(cols);
			List<Set<String>> countCardinality =  new ArrayList<>();
			char label = 'A';
			for (int i = 0; i < cols; i++) {
				list.add(new Dimension(label++));
				countCardinality.add(new HashSet<>());
			}
			
			for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
				row = sheet.getRow(r);
				if (row != null) {
					for (int c = 0; c < cols; c++) {
						cell = row.getCell((short) c);
						if (cell != null && !cell.toString().isEmpty()) {
							list.get(c).values.add(cell.toString());
							countCardinality.get(c).add(cell.toString());
						}
					}
				}
			}
			for (int i = 0; i < list.size(); i++) {
				list.get(i).cardinality = countCardinality.get(i).size();
			}
			return list;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public static void printMap(Map<String, Integer> baseTable) {
		for (Map.Entry<String, Integer> entry : baseTable.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	public static <T> void print(List<Dimension> list) {
		for (int i = 0; i < list.get(0).values.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				System.out.print(list.get(j).values.get(i) + "  ");
			}
			System.out.println();
		}
	}

	public static <T> List<List<String>> convertToString(List<List<T>> list) {
		List<List<String>> strList = new ArrayList<List<String>>();
		for (List<T> attribute : list) {
			List<String> strings = new ArrayList<String>();
			if (!attribute.isEmpty()) {
				for (T d : attribute) {
				    // Apply formatting to the string if necessary
				    strings.add(d.toString());
				}
			}
			strList.add(strings);
		}
		return strList;
	}
	
	public static void bfs(Cuboid root) {
		Queue<Cuboid> parents = new LinkedList<Cuboid>();
		parents.add(root);
		while (!parents.isEmpty()) {
			Queue<Cuboid> children = new LinkedList<Cuboid>();
			for (Cuboid cuboid : parents) {
				System.out.print(cuboid.attrValue + ":" + cuboid.aggrVal);
				for (Cuboid childCuboid : cuboid.children.values()) {
					children.add(childCuboid);
					System.out.print("\t");
				}
				if (cuboid.children.isEmpty())
					System.out.print("\t");
			}
			System.out.println();
			parents = children;
		}
	}

	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void printSubTreesFromMap(Map<String, Cuboid> map){
		for (Map.Entry<String, Cuboid> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "\t:\t" + entry.getValue().aggrVal);
		}
	}

	public static void printAllBFS(Map<String, Cuboid> map) {
		for (Cuboid cuboid : map.values()) { 
			 Util.bfs(cuboid);
			 System.out.println(""); 
		}
	}
}

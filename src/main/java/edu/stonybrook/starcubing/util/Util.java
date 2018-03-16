package edu.stonybrook.starcubing.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.stonybrook.starcubing.model.Cuboid;

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
	
	public static List<List<String>> readCSV(String path) {
		try {
			Scanner in = new Scanner(new File(path));
			String str = in.next();
			int cols = str.split(";").length;
			List<List<String>> list = new ArrayList<List<String>>();
			for (int i = 0; i < cols; i++) {
				list.add(new ArrayList<String>());
			}
			while (in.hasNext()){
				String[] strAttrs = in.next().split(";");
				/*if(null == list) {
					list = new ArrayList<List<String>>(strAttrs.length);
					for (int i = 0; i < strAttrs.length; i++) {
						list.add(new ArrayList<String>());
					}
				}*/
				for (int i = 0; i < strAttrs.length; i++) {
					list.get(i).add(strAttrs[i]);
				}
			}
			in.close();
			return list;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static List<List<String>> readXLSX(String path) {
		List<List<String>> list;
		try {
			FileInputStream excelFile = new FileInputStream(new File(path));

			/*XSSFWorkbook wb = new XSSFWorkbook(excelFile);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;*/
			
			@SuppressWarnings("resource")
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet sheet = workbook.getSheetAt(0);
			Row row;
			Cell cell;
			
			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			int cols = sheet.getRow(1).getPhysicalNumberOfCells();; // No of columns
			list = new ArrayList<List<String>>(cols);
			for (int i = 0; i < cols; i++) {
				list.add(new ArrayList<String>());
			}

			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					for (int c = 0; c < cols; c++) {
						cell = row.getCell((short) c);
						if (cell != null) {
							// Your code here
							//list.get(c).add(cell.getNumericCellValue());
							list.get(c).add(cell.toString());
						}
					}
				}
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

	public static <T> void print(List<List<T>> list) {
		for (int i = 0; i < list.get(0).size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				System.out.print(list.get(j).get(i) + "  ");
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
				System.out.print(cuboid.attrValue + ":" + cuboid.aggrVal + "\t");
				for (Cuboid childCuboid : cuboid.children.values()) {
					children.add(childCuboid);
				}
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
}

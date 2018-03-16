package edu.stonybrook.starcubing;

import java.util.List;
import java.util.Map;

import edu.stonybrook.starcubing.model.Cuboid;
import edu.stonybrook.starcubing.util.Util;

public class Main {

	private final static String PATH = "E:/sbu/course work/ESE 589/project 1/AirQualityUCI/AirQualityUCI.csv";
	// "E:/sbu/course work/ESE 589/project 1/AirQualityUCI/AirQualityUCI.xlsx";
	// "E:/sbu/course work/ESE589/project 1/AirQualityUCI/AirQualityUCI_less.xlsx";

	public static void main(String[] args) {
		//List<List<String>> list = Util.readXLSX(PATH);
		List<List<String>> list = Util.readCSV(PATH);
		//Util.print(list);
		
		/* Normalize all columns */
		DataPreProcessing.normalizeTable(list);
		//Util.print(list);

		/* Convert double lists to string */
		//List<List<String>> strList = Util.convertToString(list);

		/* replace Dimension/attribute values with **/
		Algorithm.replaceDimensions(list);
		//Util.print(list);

		/* Get compressed base table after star reduction */
		Map<String, Integer> baseTable = Algorithm.compressedBaseTable(list);
		Util.printMap(baseTable);

		/*Create star-tree and get root*/
		Cuboid root = Algorithm.cuboidTree(baseTable);
		//Util.bfs(root);
	}
}

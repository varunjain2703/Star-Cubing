package edu.stonybrook.starcubing;

import java.util.List;
import java.util.Map;

import edu.stonybrook.starcubing.model.Cuboid;
import edu.stonybrook.starcubing.model.Dimension;
import edu.stonybrook.starcubing.util.Util;

public class Main {

	//private final static String PATH = "./input/AirQualityUCI/ReportExample.csv";
	// "AirQualityUCI.xlsx","AirQualityUCI_less.xlsx","temp.xlsx",:ReportExample.csv";

	private final static String PATH = "./input/bank.csv";
	//	bank, AirQualityUCI, automobile, day

	public static void main(String[] args) {
		executeAlgoSteps();
	}

	private static void executeAlgoSteps() {
		List<Dimension> list;
		if (PATH.contains("csv"))
			list = Util.readCSV(PATH);
		else
			list = Util.readXLSX(PATH);
		// Util.print(list);

		/* Set Iceberg Condition and Minimum Support */
		Algorithm.setIBC(2);
		Algorithm.setMinSup(2);

		/*Sort dimensions based on cardinality*/
		Algorithm.sortDimensions(list);
		
		/* Normalize all columns */
		DataPreProcessing.normalizeTable(list, true);
		// Util.print(list);

		/* replace Dimension/attribute values with **/
		Algorithm.replaceDimensions(list);
		// Util.print(list);
		
		/*Sort dimensions based on cardinality*/
		Algorithm.sortDimensions(list);
		
		/* Get compressed base table after star reduction */
		Map<String, Integer> baseTable = Algorithm.compressedBaseTable(list);
		// Util.printMap(baseTable);
		//System.out.println(baseTable.size());

		/*Create star-tree and get root*/
		Cuboid root = Algorithm.cuboidTree(baseTable);
		//Util.bfs(root);
		
		/* Last step to generate sub-trees */
		Map<String, Cuboid> map = Algorithm.starCubing(root, list.size());
		//Util.printAllBFS(map);
		//Util.printSubTreesFromMap(map);
		System.out.println(map.size());
	}
}

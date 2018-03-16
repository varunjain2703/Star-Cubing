package edu.stonybrook.starcubing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stonybrook.starcubing.util.Util;

public class DataPreProcessing {

	static double startPer = 0.15,
			endPer = 0.85;
	static double startRange = 0,
			endRange = 10;

	public static void normalizeTable(List<List<String>> list) {
		for (List<String> attribute : list) {
			if (!attribute.isEmpty() && Util.isDouble(attribute.get(0))) {
				DataPreProcessing.normalizeList(attribute);
			}
		}
	}

	public static void normalizeList(List<String> list) {
		double mean,
				min = Double.MAX_VALUE,
				max = Double.MIN_VALUE;
		{
			double sum = 0,
					invalidCount = 0;
			for (int i = 0; i < list.size(); i++) {
				String l = list.get(i);
				if(l.contains(","))
					System.out.println(l);
				double temp = Double.parseDouble(list.get(i));
				if (temp != -200) {
					if (list.get(i).contains("-2"))
						System.out.println(list.get(i));
					sum += temp;
					min = min > temp ? temp : min;
					max = max < temp ? temp : max;
				} else
					invalidCount++;
			}
			mean = sum / (list.size() - invalidCount);
		}
		for (int i = 0; i < list.size(); i++) {
			double temp = Double.parseDouble(list.get(i));
			if (temp == -200) 
				list.set(i, Double.toString(normalize(mean, min, max)));
			else{
				list.set(i, Double.toString(normalize(temp, min, max)));
				if ((Double.toString(normalize(temp, min, max)).contains(",")))
					System.out.println(list.get(i));
			}
		}
	}

	public static double normalize(double value, double start, double end) {
		return ((int) ((((value - start) / (end - start)) * (endRange - startRange)) + startRange));
		//return value;
	}

	public static void normalizeList2(List<String> list) {
		List<String> sorted = new ArrayList<String>(list);
		Collections.sort(sorted);
		double mean = getMean(sorted);
		int index = sorted.indexOf(-200);
		double start = Double.parseDouble(sorted.get(0)),
				end = Double.parseDouble(sorted.get(index != -1 ? index + 1 : sorted.size() - 1));
		for (int i = 0; i < list.size(); i++) {
			double temp = Double.parseDouble(list.get(i));
			if (temp == -200)
				list.set(i, Double.toString(normalize(mean, start, end)));
			else
				list.set(i, Double.toString(normalize(temp, start, end)));
		}
	}

	/*public static void normalizeList1(List<Double> list) {
		int startRange = (int) (list.size() * startPer);
		int endRange = (int) (list.size() * endPer);
		List<Double> sorted = new ArrayList<Double>(list);
		Collections.sort(sorted);
		// double mean = getMean(sorted, startRange, endRange);
		double mean = getMean(sorted);
		double start = sorted.get(startRange),
				end = sorted.get(endRange);
		for (int i = 0; i < list.size(); i++) {
			// if (list.get(i) < start || list.get(i) > end)
			if (list.get(i) == -200)
				list.set(i, normalize(mean, start, end));
			else
				list.set(i, normalize(list.get(i), start, end));
		}
	}*/

	public static double getMean(List<String> list) {
		double sum = 0,
				invalidCount = 0;
		for (int i = 0; i < list.size(); i++) {
			double temp = Double.parseDouble(list.get(i));
			if (temp != -200)
				sum += temp;
			else
				invalidCount++;
		}
		return sum / (list.size() - invalidCount);
	}

	public static double getMean(List<Double> list, int startRange, int endRange) {
		double sum = 0;
		for (int i = startRange; i <= endRange; i++) {
			sum += list.get(i);
		}
		return sum / (endRange - startRange + 1);
	}

}

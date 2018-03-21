package edu.stonybrook.starcubing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stonybrook.starcubing.model.Dimension;
import edu.stonybrook.starcubing.util.Util;

public class DataPreProcessing {

	static double errorVal = -200;
	static double startRange = 0,
			endRange = 15;

	public static void normalizeTable(List<Dimension> list, boolean rangeBased) {
		int rangedecrement = 0;
		for (Dimension attribute : list) {
			if (!attribute.values.isEmpty() && Util.isDouble(attribute.values.get(0))) {
				if(rangeBased)
					DataPreProcessing.normalizeList(attribute, rangedecrement++);
				else
					DataPreProcessing.normalizeList(attribute, 0);
			}
		}
	}

	public static void normalizeList(Dimension attribute, int rangedecrement) {
		double mean = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		try {
			double sum = 0,
					invalidCount = 0;
			for (int i = 0; i < attribute.values.size(); i++) {
				String l = attribute.values.get(i);
				if(l.contains(","))
					System.out.println(l);
				String str = attribute.values.get(i);
				try {
					double temp = Double.parseDouble(str);
					if (temp != errorVal) {
						sum += temp;
						min = min > temp ? temp : min;
						max = max < temp ? temp : max;
					} else
						invalidCount++;
				} catch (Exception e) {
					//System.out.println(e);
				}
			}
			mean = sum / (attribute.values.size() - invalidCount);
		} catch (Exception e) {
			//System.out.println(e);
		}
		setNormalized(attribute, mean, min, max, rangedecrement);
	}

	private static void setNormalized(Dimension attribute, double mean, double min, double max, int rangedecrement) {
		for (int i = 0; i < attribute.values.size(); i++) {
			try {
				double temp = Double.parseDouble(attribute.values.get(i));
				if (temp == errorVal) 
					attribute.values.set(i, Double.toString(normalize(mean, min, max, rangedecrement)));
				else{
					attribute.values.set(i, Double.toString(normalize(temp, min, max, rangedecrement)));
					/*if ((Double.toString(normalize(temp, min, max, rangedecrement)).contains(",")))
						System.out.println(attribute.values.get(i));*/
				}
			} catch (Exception e) {
				attribute.values.set(i, Double.toString(normalize(mean, min, max, rangedecrement)));
			}
		}
	}

	public static double normalize(double value, double start, double end, int rangedecrement) {
		return ((int) ((((value - start) / (end - start)) * (endRange - startRange - rangedecrement)) + startRange));
		//return value;
	}

	public static void normalizeList2(List<String> list) {
		List<String> sorted = new ArrayList<String>(list);
		Collections.sort(sorted);
		double mean = getMean(sorted);
		int index = sorted.indexOf(errorVal);
		double start = Double.parseDouble(sorted.get(0)),
				end = Double.parseDouble(sorted.get(index != -1 ? index + 1 : sorted.size() - 1));
		for (int i = 0; i < list.size(); i++) {
			double temp = Double.parseDouble(list.get(i));
			if (temp == errorVal)
				list.set(i, Double.toString(normalize(mean, start, end, 0)));
			else
				list.set(i, Double.toString(normalize(temp, start, end, 0)));
		}
	}

	public static double getMean(List<String> list) {
		double sum = 0,
				invalidCount = 0;
		for (int i = 0; i < list.size(); i++) {
			double temp = Double.parseDouble(list.get(i));
			if (temp != errorVal)
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

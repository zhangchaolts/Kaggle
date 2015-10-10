package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalMAP {
	private static String inputpath1 = "data/track1/new_click_predict";
	private static String inputpath2 = "data/track1/new_click_actual";
	private static List<String> predictList = new ArrayList<String>();
	private static double totalMAP = 0;

	public static void main(String[] args) throws Exception {

		BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
		BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));

		int ptr = 0;
		String line;
		while ((line = in1.readLine()) != null) {
			predictList.add(line.split("\t")[1]);
			ptr++;
			if (ptr % 10000 == 0) {
				System.out.println("in1: " + ptr / 10000 + "w");
			}
		}
		in1.close();

		ptr = 0;
		while ((line = in2.readLine()) != null) {
			String[] actualClickItems = line.split("\t")[1].split(",");
			String[] predictClickItems = predictList.get(ptr).split(",");
			double map = calMAP(predictClickItems, actualClickItems);
			totalMAP += map;
			ptr++;
			if (ptr % 10000 == 0) {
				System.out.println("in1: " + ptr / 10000 + "w");
			}
		}
		in2.close();

		System.out.println("totalMAP:" + totalMAP);
		System.out.println("ptr:" + ptr);
		System.out.println("totalMAP/ptr:" + totalMAP / (double) ptr);

	}

	public static double calMAP(String[] predictClickItems, String[] actualClickItems) {
		if (actualClickItems.length == 0) {
			return 0;
		}

		Map<String, Boolean> actualClickMap = new HashMap<String, Boolean>();
		for (String click : actualClickItems) {
			actualClickMap.put(click, true);
		}

		double map = 0;
		int rec = 0;
		for (int i = 0; i < predictClickItems.length; i++) {
			if (actualClickMap.containsKey(predictClickItems[i])) {
				rec++;
				map += (double) rec / (double) i;
			}
		}

		return map / (double) rec;
	}
}

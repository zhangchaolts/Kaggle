package cn.edu.jlu.zhangc10.recsys.category;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StatisticsCategory {

	private static String inputpath1 = "data/track1/original/item.txt";
	private static Map<String, Integer> categoryItemMap = new HashMap<String, Integer>();

	public static void main(String[] args) {
		statistics1();
	}

	private static void statistics1() {

		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			String line;
			while ((line = in1.readLine()) != null) {
				String category = line.split("\t")[1];
				if (categoryItemMap.containsKey(category)) {
					categoryItemMap.put(category, categoryItemMap.get(category) + 1);
				} else {
					categoryItemMap.put(category, 1);
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Iterator<String> itr = categoryItemMap.keySet().iterator();
		while (itr.hasNext()) {
			String category = itr.next();
			int times = categoryItemMap.get(category);
			System.out.println(category + " : " + times);
		}

		System.out.println(categoryItemMap.size());

	}

}

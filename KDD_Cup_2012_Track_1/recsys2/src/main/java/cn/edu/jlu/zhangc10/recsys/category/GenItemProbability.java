package cn.edu.jlu.zhangc10.recsys.category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenItemProbability {

	private static String CategoryFeedbackPath = "data/track1/categoryFeedback";
	private static String inputPath = "data/track1/original/item.txt";
	private static String outputPath = "data/track1/itemProbability";

	private static Map<String, String> categoryFeedbackMap = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {
		init();

		BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));

		String line;
		while ((line = in1.readLine()) != null) {
			String[] terms = line.split("\t");
			String item = terms[0];
			String category = terms[1];
			String validCategory = calValidCategory(category);
			double probability = calProbability(validCategory);
			out1.write(item + "\t" + probability + "\n");
		}
		in1.close();
		out1.close();
	}

	private static void init() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(CategoryFeedbackPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String categoty = terms[0];
				String feedback = terms[1];
				categoryFeedbackMap.put(categoty, feedback);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String calValidCategory(String category) {
		String[] digits = category.split("\\.");
		List<String> categoryList = new ArrayList<String>();
		String tempString = "";
		for (int i = 0; i < digits.length; i++) {
			tempString += digits[i] + ".";
			categoryList.add(tempString.substring(0, tempString.length() - 1));
		}

		String validCategory = "";
		for (int i = categoryList.size() - 1; i >= 0; i--) {
			String subCategory = categoryList.get(i);
			if (categoryFeedbackMap.containsKey(subCategory)) {
				String[] terms = categoryFeedbackMap.get(subCategory).split(",");
				int numPositive = Integer.valueOf(terms[0]);
				if (subCategory.split("\\.").length == 4 && numPositive >= 30) {
					validCategory = subCategory;
					break;
				}
				if (subCategory.split("\\.").length == 3 && numPositive >= 50) {
					validCategory = subCategory;
					break;
				}
				if (subCategory.split("\\.").length == 2 && numPositive > 100) {
					validCategory = subCategory;
					break;
				}
				if (subCategory.split("\\.").length == 1) {
					validCategory = subCategory;
					break;
				}
			}
		}
		return validCategory;
	}

	private static double calProbability(String validCategory) {
		double pro = 0.073;
		if (!validCategory.equals("")) {
			String[] digits = validCategory.split("\\.");
			List<Double> probabilityList = new ArrayList<Double>();
			String tempString = "";
			for (int i = 0; i < digits.length; i++) {
				tempString += digits[i] + ".";
				String tempCategory = tempString.substring(0, tempString.length() - 1);
				double tempPro = 0.073;
				if (categoryFeedbackMap.containsKey(tempCategory)) {
					double posi = Double.valueOf(categoryFeedbackMap.get(tempCategory).split(",")[0]);
					double nega = Double.valueOf(categoryFeedbackMap.get(tempCategory).split(",")[1]);
					tempPro = posi / (posi + nega);
				}
				probabilityList.add(tempPro);
			}
			
			if (probabilityList.size() == 4) {
				return 0.4 * probabilityList.get(3) + 0.3 * probabilityList.get(2) + 0.2 * probabilityList.get(1) + 0.1
						* probabilityList.get(0);
			}
			if (probabilityList.size() == 3) {
				return 0.5 * probabilityList.get(2) + 0.3 * probabilityList.get(1) + 0.2 * probabilityList.get(0);
			}
			if (probabilityList.size() == 2) {
				return 0.7 * probabilityList.get(1) + 0.3 * probabilityList.get(0);
			}
			if (probabilityList.size() == 1) {
				return probabilityList.get(0);
			}
		}
		return pro;
	}
}

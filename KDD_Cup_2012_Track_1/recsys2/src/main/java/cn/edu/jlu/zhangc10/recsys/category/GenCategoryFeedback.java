package cn.edu.jlu.zhangc10.recsys.category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Cell {
	int numPositive;
	int numNegative;

	public Cell(int numPositive, int numNegative) {
		this.numPositive = numPositive;
		this.numNegative = numNegative;
	}
}

public class GenCategoryFeedback {

	private static String itemCategoryPath = "data/track1/original/item.txt";
	private static String trainPath = "data/track1/original/rec_log_train.txt";
	private static String outputPath = "data/track1/categoryFeedback";

	private static Map<String, String> itemCategoryMap = new HashMap<String, String>();
	private static Map<String, Cell> categoryFeedbackMap = new HashMap<String, Cell>();

	public static void main(String[] args) throws Exception {
		init();

		BufferedReader in1 = new BufferedReader(new FileReader(trainPath));
		String line;
		int ptr = 0;
		while ((line = in1.readLine()) != null) {
			String[] terms = line.split("\t");
			String item = terms[1];
			String label = terms[2];

			if (!itemCategoryMap.containsKey(item)) {
				continue;
			}
			String[] digits = itemCategoryMap.get(item).split("\\.");
			List<String> categoryList = new ArrayList<String>();
			String tempString = "";
			for (int i = 0; i < digits.length; i++) {
				tempString += digits[i] + ".";
				categoryList.add(tempString.substring(0, tempString.length() - 1));
			}

			Cell cell = null;
			if (label.equals("1")) {
				cell = new Cell(1, 0);
			} else {
				cell = new Cell(0, 1);
			}

			for (String category : categoryList) {
				if (categoryFeedbackMap.containsKey(category)) {
					Cell exsitCell = categoryFeedbackMap.get(category);
					categoryFeedbackMap.put(category, new Cell(exsitCell.numPositive + cell.numPositive,
							exsitCell.numNegative + cell.numNegative));
				} else {
					categoryFeedbackMap.put(category, cell);
				}
			}

			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}
		}
		in1.close();

		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));
		Iterator<String> itr = categoryFeedbackMap.keySet().iterator();
		while (itr.hasNext()) {
			String category = itr.next();
			Cell cell = categoryFeedbackMap.get(category);
			out1.write(category + "\t" + cell.numPositive + "," + cell.numNegative + "\n");
		}
		out1.close();
	}

	private static void init() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(itemCategoryPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String item = terms[0];
				String category = terms[1];
				itemCategoryMap.put(item, category);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

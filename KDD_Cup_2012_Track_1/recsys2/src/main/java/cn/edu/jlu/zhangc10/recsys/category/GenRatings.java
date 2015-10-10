package cn.edu.jlu.zhangc10.recsys.category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class GenRatings {

	private static String itemProbabilityPath = "data/track1/itemProbability";
//	private static String testPath = "data/track1/test";
//	private static String outputPath = "data/track1/sna_ratings";
//	private static String testPath = "data/track1/kddcup_test";
//	private static String outputPath = "data/track1/sna_ratings";
	private static String testPath = "data/track1/original/rec_log_test.txt";
	private static String outputPath = "data/track1/sna_submission_ratings";
	
	private static Map<String, Double> itemProbabilityMap = new HashMap<String, Double>();

	public static void main(String[] args) throws Exception {

		init();

		BufferedReader in1 = new BufferedReader(new FileReader(testPath));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));

		String line;
		int ptr = 0;
		while ((line = in1.readLine()) != null) {
			String[] terms = line.split("\t");
			String user = terms[0];
			String item = terms[1];
			String feedback = terms[2];

			double pro = 0.073;
			if (itemProbabilityMap.containsKey(item)) {
				pro = itemProbabilityMap.get(item);
			}

			out1.write(user + "\t" + item + "\t" + feedback + "\t" + pro + "\n");

			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}
		}
		in1.close();
		out1.close();
	}

	private static void init() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(itemProbabilityPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String item = terms[0];
				double probability = Double.valueOf(terms[1]);
				itemProbabilityMap.put(item, probability);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package cn.edu.jlu.zhangc10.recsys.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class GenSample {

	private static String inputpath1 = "data/track1/itemWordsNumberMap";
	private static String inputpath2 = "data/track1/itemInfoMap";
	private static Map<String, String> itemWordsNumberMap = new HashMap<String, String>();
	private static Map<String, String> itemInfoMap = new HashMap<String, String>();
	private static String inputpath = "data/track1/result2";
	private static String outputPath = "data/track1/sample.csv";
	private static int limit = 1000000;

	public static void main(String[] args) {

		init();

		try {
			String line;
			int ptr = 0;
			BufferedReader in = new BufferedReader(new FileReader(inputpath));
			BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
			out.write("f1,f2,f3,f4,f5\n");
			while ((line = in.readLine()) != null) {
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
				if (ptr >= limit) {
					break;
				}
				String[] terms = line.split("\t");
				String iid = terms[1];
				String wordsNumber = "0";
				if (itemWordsNumberMap.containsKey(iid)) {
					wordsNumber = itemWordsNumberMap.get(iid);
				}
				String wordInfo = "0,0";
				if (itemInfoMap.containsKey(iid)) {
					wordInfo = itemInfoMap.get(iid);
				}
				String[] subTerms = wordInfo.split(",");
				long positive = Long.valueOf(subTerms[0]);
				long negative = Long.valueOf(subTerms[1]);

				String label = "Yes";
				if (terms[2].equals("-1")) {
					label = "No";
				}

				out.write(terms[3] + "," + wordsNumber + "," + (positive + negative) + "," + (double) (positive + 100)
						/ (double) (positive + negative + 1000) + "," + label + "\n");
				ptr++;
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init() {
		try {
			String line;

			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				itemWordsNumberMap.put(terms[0], terms[1]);
			}
			in1.close();

			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split("\t");
				itemInfoMap.put(terms[0], terms[1] + "," + terms[2]);
			}
			in2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package cn.edu.jlu.zhangc10.kddcup.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenSamples {
	private static String itemInfoPath = "data/track1/combine_itemInfoMap";
	private static String userInfoPath = "data/track1/combine_userInfoMap";
	private static String snaRatingsPath = "data/track1/sna_ratings";
	private static String inputpath = "data/track1/ratings";
	private static String outputPath1 = "data/track1/combine_smaples_train.csv";
	private static String outputPath2 = "data/track1/combine_smaples_test.csv";

	private static Map<String, String> itemInfoMap = new HashMap<String, String>();
	private static Map<String, String> userInfoMap = new HashMap<String, String>();
	private static List<Double> snaRatingsList = new ArrayList<Double>();
	private static int limit = 10000000;
	

	public static void main(String[] args) throws Exception {

		init();

		BufferedReader in = new BufferedReader(new FileReader(inputpath));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath1));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(outputPath2));
		out1.write("f1,f2,f3,f4,f5\n");
		out2.write("f1,f2,f3,f4,f5\n");
		String line;
		int ptr = 0;
		while ((line = in.readLine()) != null) {
			String[] terms = line.split("\t");
			String user = terms[0];
			String item = terms[1];
			String label = terms[2];
			String rating = terms[3];

			double snaRating = snaRatingsList.get(ptr);

			double userAvg = 0.073;
			if (userInfoMap.containsKey(user)) {
				String[] subTerms = userInfoMap.get(user).split(",");
				userAvg = (Double.valueOf(subTerms[0]) + 7.0)
						/ (Double.valueOf(subTerms[0]) + Double.valueOf(subTerms[1]) + 100.0);
			}

			double itemAvg = 0.073;
			if (itemInfoMap.containsKey(item)) {
				String[] subTerms = itemInfoMap.get(item).split(",");
				itemAvg = (Double.valueOf(subTerms[0]) + 7.0)
						/ (Double.valueOf(subTerms[0]) + Double.valueOf(subTerms[1]) + 100.0);
			}

			String weka_label = "Yes";
			if (label.equals("-1")) {
				weka_label = "No";
			}
			if (ptr < limit / 2) {
				out1.write(rating + "," + snaRating + "," + userAvg + "," + itemAvg + "," + weka_label + "\n");
			} else {
				out2.write(rating + "," + snaRating + "," + userAvg + "," + itemAvg + "," + weka_label + "\n");
			}

			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("mian() : " + ptr / 1000000 + "00w");
			}
			if (ptr == limit) {
				break;
			}
		}
		in.close();
		out1.close();
		out2.close();
	}

	public static void init() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(itemInfoPath));
			BufferedReader in2 = new BufferedReader(new FileReader(userInfoPath));
			BufferedReader in3 = new BufferedReader(new FileReader(snaRatingsPath));

			String line;
			int ptr = 0;

			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				itemInfoMap.put(terms[0], terms[1]);
			}
			in1.close();

			ptr = 0;
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split("\t");
				userInfoMap.put(terms[0], terms[1]);
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println("init() in2 : " + ptr / 1000000 + "00w");
				}
			}
			in2.close();

			ptr = 0;
			while ((line = in3.readLine()) != null) {
				snaRatingsList.add(Double.valueOf(line.split("\t")[3]));
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println("init() in3 : " + ptr / 1000000 + "00w");
				}
			}
			in3.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

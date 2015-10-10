package cn.edu.jlu.zhangc10.recsys.classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Classify {

	private static String ratingsPath = "data/track1/ratings";
	private static String classificationPath = "data/track1/classification";

	private static List<Double> ratingsList = new ArrayList<Double>();
	private static final double proportion = 0.07177583417314343;

	public static void main(String[] args) throws Exception {
		String line;
		int ptr = 0;
		BufferedReader in1 = new BufferedReader(new FileReader(ratingsPath));

		while ((line = in1.readLine()) != null) {
			ratingsList.add(Double.valueOf(line.split("\t")[3]));
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("in1: " + ptr / 1000000 + "00w");
			}
		}
		in1.close();

		Collections.sort(ratingsList);
		double threshold = ratingsList.get((int) ((1.0 - proportion) * ratingsList.size()));

		BufferedReader in2 = new BufferedReader(new FileReader(ratingsPath));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(classificationPath));
		ptr = 0;
		while ((line = in2.readLine()) != null) {
			double rating = Double.valueOf(line.split("\t")[3]);
			if (rating > threshold) {
				out1.write(line + "\t" + 1 + "\n");
			} else {
				out1.write(line + "\t" + -1 + "\n");
			}
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("in2: " + ptr / 1000000 + "00w");
			}
		}
		in2.close();
		out1.close();
	}
}

package cn.edu.jlu.zhangc10.kddcup.localtest;

import java.io.BufferedReader;
import java.io.FileReader;

public class StatisticsClassification {

	private static String classificationPath = "data/track1/classification";

	private static int actualPositive = 0;
	private static int actualNegative = 0;
	private static int predictPositive = 0;
	private static int predictNegative = 0;
	private static int positiveRight = 0;
	private static int negativeRight = 0;
	private static double sum = 0;

	public static void main(String[] args) throws Exception {

		BufferedReader in1 = new BufferedReader(new FileReader(classificationPath));
		int ptr = 0;
		String line;
		while ((line = in1.readLine()) != null) {
			String[] terms = line.split("\t");
			String actual = terms[2];
			String predict = terms[4];
			if (actual.equals("1")) {
				actualPositive++;
			} else {
				actualNegative++;
			}
			if (predict.equals("1")) {
				predictPositive++;
			} else {
				predictNegative++;
			}
			if (actual.equals("1") && actual.equals(predict)) {
				positiveRight++;
			}
			if (actual.equals("-1") && actual.equals(predict)) {
				negativeRight++;
			}
			if (!actual.equals(predict)) {
				sum += Math.pow(2.0, 2.0);
			}
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}
		}
		in1.close();

		System.out.println("\nAccuracy:" + (double) (positiveRight + negativeRight) / (double) ptr);
		System.out.println("Rmse:" + Math.sqrt(sum / (double) ptr));
	}

}

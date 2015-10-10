package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class statistics {
	
	private static String result1Path = "data/track1/result1";
	private static String result2Path = "data/track1/result2";
	
	public static void main(String[] args) {
		//predict();
		stat();
	}

	static void predict() {
		List<Float> scoreList = new ArrayList<Float>();
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(result1Path));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if(ptr % 10000 == 0) {
					System.out.println(ptr/10000 + "w");
				}
				if(ptr > 10000000) {
					break;
				}
				scoreList.add(Float.valueOf(line.split("\t")[3]));
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(scoreList);
		
		double proportion = 0.07177583417314343;
		float threshold = scoreList.get((int) ((1.0-proportion) * scoreList.size()));

		try {
			BufferedReader in1 = new BufferedReader(new FileReader(result1Path));
			BufferedWriter out1 = new BufferedWriter(
					new FileWriter(result2Path));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if(ptr % 10000 == 0) {
					System.out.println(ptr/10000 + "w");
				}
				if(ptr > 10000000) {
					break;
				}
				float score = Float.valueOf(line.split("\t")[3]);
				if (score > threshold) {
					out1.write(line + "\t" + 1 + "\n");
				} else {
					out1.write(line + "\t" + -1 + "\n");
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void stat() {
		int actualPositive = 0;
		int actualNegative = 0;
		int predictPositive = 0;
		int predictNegative = 0;
		int positiveRight = 0;
		int negativeRight = 0;
		double sum = 0;
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(result2Path));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if(ptr % 10000 == 0) {
					System.out.println(ptr/10000 + "w");
				}
				if(ptr > 10000000) {
					break;
				}
				String[] terms = line.split("\t");
				String actual = terms[2];
				String predict = terms[4];
				if(actual.equals("1")) {
					actualPositive++;
				} else {
					actualNegative++;
				}
				if(predict.equals("1")) {
					predictPositive++;
				} else {
					predictNegative++;
				}
				if(actual.equals("1") && actual.equals(predict)) {
					positiveRight++;
				}
				if(actual.equals("-1") && actual.equals(predict)) {
					negativeRight++;
				} 
				if(!actual.equals(predict))	{
					sum += Math.pow(2.0, 2.0);
				}
			}
			in1.close();
			System.out.println("actualPositive:" + actualPositive);
			System.out.println("actualNegative:" + actualNegative);
			System.out.println("predictPositive:" + predictPositive);
			System.out.println("predictNegative:" + predictNegative);
			System.out.println("positiveRight:" + positiveRight);
			System.out.println("negativeRight:" + negativeRight);
			System.out.println("sum:" + sum);
			System.out.println("rmse:" + Math.sqrt(sum/10000000.0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

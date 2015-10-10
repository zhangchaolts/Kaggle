package cn.edu.jlu.zhangc10.kddcup.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class GenTrainAndTest {

	private static String inputpath1 = "data/track1/original/rec_log_train.txt";
	private static String outputpath1 = "data/track1/train";
	private static String outputpath2 = "data/track1/test";
	private static double threshold = 0.8;

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outputpath2));
			long ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				if (Math.random() <= threshold) {
					out1.write(terms[0] + "\t" + terms[1] + "\t" + terms[2] + "\n");
				} else {
					out2.write(terms[0] + "\t" + terms[1] + "\t" + terms[2] + "\n");
				}
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
			}
			in1.close();
			out1.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

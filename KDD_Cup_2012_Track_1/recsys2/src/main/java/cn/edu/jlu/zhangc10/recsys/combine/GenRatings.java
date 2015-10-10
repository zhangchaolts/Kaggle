package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GenRatings {

	private static String inputPath1 = "data/track1/cb_result";
	private static String inputPath2 = "data/track1/result_final";
	private static String outputPath = "data/track1/cb_result_final";
	private static List<String> scoreList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		String line;
		int ptr;

		BufferedReader in1 = new BufferedReader(new FileReader(inputPath1));
		ptr = 0;
		while ((line = in1.readLine()) != null) {
			if ((ptr + 1) % 10000 == 0) {
				System.out.println((ptr + 1) / 10000 + "w");
			}
			scoreList.add(line);
			ptr++;
		}
		in1.close();

		BufferedReader in2 = new BufferedReader(new FileReader(inputPath2));
		BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
		ptr = 0;
		while ((line = in2.readLine()) != null) {
			if ((ptr + 1) % 10000 == 0) {
				System.out.println((ptr + 1) / 10000 + "w");
			}
			String[] terms = line.split("\t");
			out.write(terms[0] + "\t" + terms[1] + "\t" + terms[2] + "\t" + terms[3] + "\t" + scoreList.get(ptr) + "\n");
			ptr++;
		}
		in2.close();
		out.close();
	}
}

package cn.edu.jlu.zhangc10.kddcup.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ConvertCSV2ARFF {

	private static String csvFilePath = "data/track1/combine_test_instance.csv";
	private static String arffFilePath = "data/track1/combine_test_instance.arff";
	private static String relation = "combine_test_instance";
	private static String[] attributes = { "f1:numeric", "f2:numeric", "f3:numeric", "f4:numeric", "f5:{No,Yes}" };

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(csvFilePath));
		BufferedWriter out = new BufferedWriter(new FileWriter(arffFilePath));

		out.write("@relation " + relation + "\n\n");
		for (int i = 0; i < attributes.length; i++) {
			out.write("@attribute " + attributes[i].split(":")[0] + " " + attributes[i].split(":")[1] + "\n");
		}
		out.write("\n@data\n");

		int ptr = 0;
		String line = in.readLine();
		while ((line = in.readLine()) != null) {
			String[] terms = line.split(",");
			for (int i = 0; i < terms.length - 1; i++) {
				out.write(String.format("%.4f", Double.valueOf(terms[i])) + ",");
			}
			out.write(terms[terms.length - 1] + "\n");
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}
		}
		in.close();
		out.close();
	}
}

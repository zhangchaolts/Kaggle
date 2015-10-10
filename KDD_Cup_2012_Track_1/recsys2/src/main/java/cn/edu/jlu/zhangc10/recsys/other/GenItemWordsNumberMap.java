package cn.edu.jlu.zhangc10.recsys.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class GenItemWordsNumberMap {

	private static String inputpath1 = "data/track1/original/item.txt";
	private static String outputpath1 = "data/track1/itemWordsNumberMap";

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String iid = terms[0];
				String[] words = terms[2].split(";");
				out1.write(iid + "\t" + words.length + "\n");
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package cn.edu.jlu.zhangc10.recsys.sna.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ConvertSampleToWekaData {

	private static String inputpath1 = "data/track1/user_sns_samples";
	private static String outputpath1 = "data/track1/weka_user_sns_samples.csv";

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String line;

			out1.write("f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11\n");

			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\\|");
				String label = terms[0].split(";")[0];
				String[] features = terms[1].split(";");
				for (int i = 0; i < features.length; i++) {
					if (i == 9) {
						continue;
					}
					out1.write(features[i] + ",");
				}
				if (label.equals("true")) {
					out1.write("Yes\n");
				} else {
					out1.write("No\n");
				}
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

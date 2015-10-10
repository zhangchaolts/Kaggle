package cn.edu.jlu.zhangc10.recsys.preprocess1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GetUserTagsGroupFromLDA {

	private static String inputpath1 = "data/track1/user_tags";
	private static String inputpath2 = "data/track1/model-final.theta";
	private static String outputpath1 = "data/track1/user_tags_group";
	private static List<String> uidList = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				uidList.add(uid);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println("in1: " + ptr / 10000 + "w");
				}
			}
			in1.close();
			ptr = 0;
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split(" ");
				double maxValue = -1.0;
				int group = -1;
				for (int i = 0; i < terms.length; i++) {
					double value = Double.valueOf(terms[i]);
					if (value > maxValue) {
						group = i + 1;
						maxValue = value;
					}
				}
				out1.write(uidList.get(ptr) + "\t" + group + "\n");
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println("in2: " + ptr / 10000 + "w");
				}
			}
			in2.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

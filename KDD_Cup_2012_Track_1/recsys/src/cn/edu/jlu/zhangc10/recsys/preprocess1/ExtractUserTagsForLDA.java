package cn.edu.jlu.zhangc10.recsys.preprocess1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ExtractUserTagsForLDA {

	private static String inputpath1 = "data/track1/original/user_profile.txt";
	private static String outputpath1 = "data/track1/user_tags";
	private static String outputpath2 = "data/track1/user_tags_LDAFormat";
	private static List<String> tagsInfoList = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outputpath2));

			long ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String tags = terms[4].replaceAll(";", " ");
				if (tags.equals("0")) {
					continue;
				}
				out1.write(uid + "\t" + tags + "\n");
				tagsInfoList.add(tags);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
			}

			out2.write(tagsInfoList.size() + "\n");
			for (String tags : tagsInfoList) {
				out2.write(tags + "\n");
			}

			in1.close();
			out1.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

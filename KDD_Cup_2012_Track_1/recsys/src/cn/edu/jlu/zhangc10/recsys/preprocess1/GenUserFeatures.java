package cn.edu.jlu.zhangc10.recsys.preprocess1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class GenUserFeatures {

	private static String inputpath1 = "data/track1/user_tags_group";
	private static String inputpath2 = "data/track1/original/user_profile.txt";
	private static String outputpath1 = "data/track1/user_features";

	private static Map<String, String> userTagsGroupMap = new HashMap<String, String>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String uid = line.split("\t")[0];
				String group = line.split("\t")[1];
				userTagsGroupMap.put(uid, group);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println("in1: " + ptr / 10000 + "w");
				}
			}
			in1.close();

			ptr = 0;
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String birth = terms[1];
				String sex = terms[2];
				String numWeibos = terms[3];
				String tagsGroup = "0";
				//处理异常数据
				if(sex.equals("3")) {
					sex = "0";
				}
				if (userTagsGroupMap.containsKey(uid)) {
					tagsGroup = userTagsGroupMap.get(uid);
				}
				out1.write(uid + "\t" + changBirthInfo(birth) + "\t" + sex + "\t" + changNumWeibosInfo(numWeibos)
						+ "\t" + tagsGroup + "\n");
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

	private static String changBirthInfo(String birth) {
		if (birth.contains("-")) {
			return "0";
		}
		int birthValue = Integer.valueOf(birth);
		if (birthValue > 2012) {
			return "14";
		}
		int ans = (2012 - birthValue) / 5 + 1;
		if (ans > 12) {
			ans = 13;
		}
		return String.valueOf(ans);
	}

	private static String changNumWeibosInfo(String numWeibos) {
		int number = Integer.valueOf(numWeibos);
		if (number == 0) {
			return "0";
		}
		if (number <= 30) {
			return "1";
		}
		if (number <= 100) {
			return "2";
		}
		if (number <= 300) {
			return "3";
		}
		return "4";
	}
}

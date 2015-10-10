package cn.edu.jlu.zhangc10.kddcup.sna.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GenUserSnsChange {

	private static String inputpath1 = "data/track1/original/user_sns.txt";
	private static String outputpath1 = "data/track1/user_sns_change";

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String lastUid = "";
			String line;
			List<String> userList = new ArrayList<String>();

			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid1 = terms[0];
				String uid2 = terms[1];
				if (!uid1.equals(lastUid)) {
					if (!lastUid.equals("")) {
						out1.write(lastUid + "\t" + userList.size() + ":");
						for (String uid : userList) {
							out1.write(uid + ";");
						}
						out1.write("\n");
					}
					userList.clear();
					userList.add(uid2);
					lastUid = uid1;
				} else {
					userList.add(uid2);
				}
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

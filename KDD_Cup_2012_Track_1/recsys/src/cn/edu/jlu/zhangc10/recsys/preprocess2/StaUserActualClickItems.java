package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class StaUserActualClickItems {

	private static String inputpath1 = "data/track1/new_test_sort";
	private static String outputpath1 = "data/track1/new_click_actual";

	public static void main(String[] args) throws Exception {
		BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

		String lastUser = "";
		List<String> clickItemList = new ArrayList<String>();

		long ptr = 0;
		String line;
		while ((line = in1.readLine()) != null) {
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}

			String[] terms = line.split("\t");
			String user = terms[0];
			String item = terms[1];
			String feedback = terms[2];

			if (!user.equals(lastUser)) {
				if (clickItemList.size() == 0) {
					out1.write(user + "\n");
				} else {
					out1.write(user + "\t");
					for (int i = 0; i < clickItemList.size(); i++) {
						out1.write(clickItemList.get(i) + ",");
					}
					out1.write("\n");
				}
				lastUser = user;
				clickItemList.clear();
			}

			if (feedback.equals("-1")) {
				continue;
			}

			clickItemList.add(item);
		}
		in1.close();
		out1.close();
	}
}

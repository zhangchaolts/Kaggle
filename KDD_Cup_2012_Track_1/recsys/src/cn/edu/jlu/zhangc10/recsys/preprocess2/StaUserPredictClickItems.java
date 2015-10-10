package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ClickItemInfo implements Comparable<ClickItemInfo> {
	String item;
	double score;

	public ClickItemInfo(String item, double score) {
		this.item = item;
		this.score = score;
	}

	@Override
	public int compareTo(ClickItemInfo c) {
		if (score < c.score) {
			return 1;
		} else {
			return -1;
		}
	}
}

public class StaUserPredictClickItems {

	private static String inputpath1 = "data/track1/new_ratings";
	private static String outputpath1 = "data/track1/new_click_predict";

	public static void main(String[] args) throws Exception {
		BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

		String lastUser = "";
		List<ClickItemInfo> clickItemInfoList = new ArrayList<ClickItemInfo>();

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
			double score = Double.valueOf(terms[4]);

			if (!user.equals(lastUser)) {
				Collections.sort(clickItemInfoList);
				
				out1.write(user + "\t");
				for (int i = 0; i < 3 && i < clickItemInfoList.size(); i++) {
					out1.write(clickItemInfoList.get(i).item + "-" + clickItemInfoList.get(i).score + ",");
				}
				out1.write("\n");

				lastUser = user;
				clickItemInfoList.clear();
			}

			clickItemInfoList.add(new ClickItemInfo(item, score));
		}
		in1.close();
		out1.close();
	}
}

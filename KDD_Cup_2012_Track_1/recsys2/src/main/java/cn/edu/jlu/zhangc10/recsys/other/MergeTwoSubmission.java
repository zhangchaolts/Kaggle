package cn.edu.jlu.zhangc10.recsys.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class MergeTwoSubmission {
	private static String inputpath1 = "data/track1/nb_sub_small_header.csv";
	private static String inputpath2 = "data/track1/rs_submission.csv";
	private static String outputpath1 = "data/track1/combine_submission1.csv";
	private static List<String> list = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			String line;
			int ptr = 0;

			while ((line = in1.readLine()) != null) {
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
				if (ptr == 0) {
					ptr++;
					continue;
				}
				list.add(line.split(",")[1]);
				ptr++;
			}
			in1.close();

			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			out1.write("id,clicks\n");

			ptr = 0;
			while ((line = in2.readLine()) != null) {
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
				if (ptr == 0) {
					ptr++;
					continue;
				}
				String[] terms = line.split(",");
				String user = terms[0];
				String[] items = terms[1].split(" ");
				if (items.length < 3) {
					out1.write(line + "\n");
				} else {
					String aItem = items[0];
					String bItem = items[1];
					String cItem = items[2];
					String[] nbItems = list.get(ptr - 1).split(" ");
					for (int i = 0; i < Math.min(nbItems.length, 2); i++) {
						if (!nbItems[i].equals(aItem) && !nbItems[i].equals(bItem)) {
							cItem = nbItems[i];
							break;
						}
					}
					out1.write(user + "," + aItem + " " + bItem + " " + cItem + "\n");
				}
				ptr++;
			}
			in2.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

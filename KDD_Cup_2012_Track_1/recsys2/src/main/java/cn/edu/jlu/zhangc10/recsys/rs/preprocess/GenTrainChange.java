package cn.edu.jlu.zhangc10.recsys.rs.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GenTrainChange {

	private static String inputpath1 = "data/track1/train_sort";
	private static String outputpath1 = "data/track1/train_change";

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String lastUid = "";
			String line;
			Map<String, Integer> itemMap = new HashMap<String, Integer>();

			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println(ptr);
				}

				String[] terms = line.split("\t");
				String uid = terms[0];
				String item = terms[1];
				String label = terms[2];

				if (uid.equals(lastUid)) {
					if (itemMap.containsKey(item + "," + label)) {
						int exsitValue = itemMap.get(item + "," + label);
						itemMap.put(item + "," + label, exsitValue + 1);
					} else {
						itemMap.put(item + "," + label, 1);
					}
				} else {
					if (!lastUid.equals("")) {
						out1.write(lastUid + "\t");
						Iterator<String> itr = itemMap.keySet().iterator();
						while (itr.hasNext()) {
							String key = itr.next();
							int value = itemMap.get(key);
							int value_change = (int) (Math.log(value) / Math.log(2.0)) + 1;
							out1.write(key + ":" + value_change + ";");
						}
						out1.write("\n");
						itemMap.clear();
					}
					itemMap.put(item + "," + label, 1);
					lastUid = uid;
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

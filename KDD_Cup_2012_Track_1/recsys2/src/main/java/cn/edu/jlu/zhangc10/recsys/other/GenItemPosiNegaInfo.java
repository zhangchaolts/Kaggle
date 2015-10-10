package cn.edu.jlu.zhangc10.recsys.other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Cell {
	long positive;
	long negative;

	public Cell(long positive, long negative) {
		this.positive = positive;
		this.negative = negative;
	}
}

public class GenItemPosiNegaInfo {

	private static String inputpath1 = "data/track1/original/rec_log_train.txt";
	private static String outputpath1 = "data/track1/itemInfoMap";
	private static Map<String, Cell> itemInfoMap = new HashMap<String, Cell>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 100000 == 0) {
					System.out.println(ptr / 100000 + "0w");
				}
				String[] terms = line.split("\t");
				String iid = terms[1];
				String feedback = terms[2];
				if (itemInfoMap.containsKey(iid)) {
					long positive = itemInfoMap.get(iid).positive;
					long negative = itemInfoMap.get(iid).negative;
					if (feedback.equals("1")) {
						itemInfoMap.put(iid, new Cell(positive + 1, negative));
					} else {
						itemInfoMap.put(iid, new Cell(positive, negative + 1));
					}
				} else {
					if (feedback.equals("1")) {
						itemInfoMap.put(iid, new Cell(1, 0));
					} else {
						itemInfoMap.put(iid, new Cell(0, 1));
					}
				}
			}
			in1.close();

			Iterator<String> itr = itemInfoMap.keySet().iterator();
			while (itr.hasNext()) {
				String iid = itr.next();
				Cell cell = itemInfoMap.get(iid);
				out1.write(iid + "\t" + cell.positive + "\t" + cell.negative + "\n");
			}
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

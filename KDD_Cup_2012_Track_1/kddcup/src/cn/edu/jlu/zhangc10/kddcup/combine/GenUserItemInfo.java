package cn.edu.jlu.zhangc10.kddcup.combine;

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

public class GenUserItemInfo {

	private static String inputpath1 = "data/track1/train";
	private static String outputpath1 = "data/track1/itemInfoMap";
	private static String outputpath2 = "data/track1/userInfoMap";
	private static Map<String, Cell> itemInfoMap = new HashMap<String, Cell>();
	private static Map<String, Cell> userInfoMap = new HashMap<String, Cell>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outputpath2));

			String line;
			int ptr = 0;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String feedback = terms[2];
				if (itemInfoMap.containsKey(item)) {
					long positive = itemInfoMap.get(item).positive;
					long negative = itemInfoMap.get(item).negative;
					if (feedback.equals("1")) {
						itemInfoMap.put(item, new Cell(positive + 1, negative));
					} else {
						itemInfoMap.put(item, new Cell(positive, negative + 1));
					}
				} else {
					if (feedback.equals("1")) {
						itemInfoMap.put(item, new Cell(1, 0));
					} else {
						itemInfoMap.put(item, new Cell(0, 1));
					}
				}
				if (userInfoMap.containsKey(user)) {
					long positive = userInfoMap.get(user).positive;
					long negative = userInfoMap.get(user).negative;
					if (feedback.equals("1")) {
						userInfoMap.put(user, new Cell(positive + 1, negative));
					} else {
						userInfoMap.put(user, new Cell(positive, negative + 1));
					}
				} else {
					if (feedback.equals("1")) {
						userInfoMap.put(user, new Cell(1, 0));
					} else {
						userInfoMap.put(user, new Cell(0, 1));
					}
				}
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
			}
			in1.close();

			Iterator<String> itr = itemInfoMap.keySet().iterator();
			while (itr.hasNext()) {
				String item = itr.next();
				Cell cell = itemInfoMap.get(item);
				out1.write(item + "\t" + cell.positive + "," + cell.negative + "\n");
			}
			out1.close();

			itr = userInfoMap.keySet().iterator();
			while (itr.hasNext()) {
				String user = itr.next();
				Cell cell = userInfoMap.get(user);
				out2.write(user + "\t" + cell.positive + "," + cell.negative + "\n");
			}
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

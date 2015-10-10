package cn.edu.jlu.zhangc10.kddcup.sna.cal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Cell {
	String node;
	double similarity;

	public Cell(String node, double similarity) {
		this.node = node;
		this.similarity = similarity;
	}
}

public class calUsersRatings {

	private static String inputpath1 = "data/track1/user_sns_similarity";
	private static Map<String, Integer> uidPtrMap1 = new HashMap<String, Integer>();
	private static Map<Integer, String> ptrUidMap1 = new HashMap<Integer, String>();
	private static List<ArrayList<Cell>> userInfoList1 = new ArrayList<ArrayList<Cell>>();
	private static String inputpath2 = "data/track1/train_change";
	private static Map<String, Integer> uidPtrMap2 = new HashMap<String, Integer>();
	private static Map<Integer, String> ptrUidMap2 = new HashMap<Integer, String>();
	private static List<ArrayList<String>> userInfoList2 = new ArrayList<ArrayList<String>>();
	private static String inputpath = "data/track1/test";
	private static String outputpath = "data/track1/sna_ratings";
	private static double r;

	public static void main(String[] args) {
		init1();
		init2();
		cal();
	}

	static void init1() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String info = terms[1];
				uidPtrMap1.put(uid, ptr);
				ptrUidMap1.put(ptr, uid);
				String[] subTerms = info.split(";");
				ArrayList<Cell> tempList = new ArrayList<Cell>();
				for (int i = 0; i < subTerms.length; i++) {
					String node = subTerms[i].split(":")[0];
					double s = Double.valueOf(subTerms[i].split(":")[1]);
					if (filter(s) == true) {
						continue;
					}
					tempList.add(new Cell(node, s));
				}
				userInfoList1.add(tempList);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println("in1:" + ptr / 10000 + "w");
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void init2() {
		try {
			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			int ptr = 0;
			String line;
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String info = terms[1];
				uidPtrMap2.put(uid, ptr);
				ptrUidMap2.put(ptr, uid);
				String[] subTerms = info.split(";");
				ArrayList<String> tempList = new ArrayList<String>();
				for (int i = 0; i < subTerms.length; i++) {
					if (subTerms[i].split(":")[0].split(",")[1].equals("1")) {
						tempList.add(subTerms[i].split(":")[0].split(",")[0]);
					}
				}
				userInfoList2.add(tempList);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println("in2:" + ptr / 10000 + "w");
				}
			}
			in2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void cal() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String feedback = terms[2];
				r = 0;
				check(user);
				if (uidPtrMap1.containsKey(user)) {
					int ptr = uidPtrMap1.get(user);
					if (userInfoList1.contains(ptr)) {
						ArrayList<Cell> cellList = userInfoList1.get(ptr);
						for (Cell cell : cellList) {
							String uid2 = cell.node;
							double similarity = cell.similarity;
							if (uidPtrMap2.containsKey(uid2)) {
								int ptr2 = uidPtrMap2.get(uid2);
								ArrayList<String> itemList = userInfoList2.get(ptr2);
								for (String item2 : itemList) {
									if (item.equals(item2)) {
										r += similarity;
										break;
									}
								}
							}
						}
					}
				}
				if(r == 0) {
					r = 0.73 + 0.01 * Math.random();
				}
				out1.write(user + "\t" + item + "\t" + feedback + "\t" + r + "\n");
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static boolean filter(double s) {
		double threshold = 0.9;
		if (s < threshold) {
			return true;
		}
		return false;
	}
	
	static void check(String uid) {
		if(!uidPtrMap1.containsKey(uid)) {
			//System.out.println("uid:" + uid + " is not exsit.");
		}
		r = Predict.newInstance();
	}
}

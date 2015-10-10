package cn.edu.jlu.zhangc10.recsys.sna.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenUserSnsSamples {

	private static String inputpath1 = "data/track1/user_sns_change";
	private static String outputpath1 = "data/track1/user_sns_samples_part1";
	private static String outputpath2 = "data/track1/user_sns_samples_part2";
	private static Map<String, Integer> uidPtrMap = new HashMap<String, Integer>();
	private static Map<Integer, String> ptrUidMap = new HashMap<Integer, String>();
	private static List<HashSet<String>> userInfoList = new ArrayList<HashSet<String>>();
	private static final int limit = 10000;

	public static void main(String[] args) {
		init();
		gen(true, outputpath1);
		gen(false, outputpath2);
	}

	static void init() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String info = terms[1];
				uidPtrMap.put(uid, ptr);
				ptrUidMap.put(ptr, uid);
				HashSet<String> set = new HashSet<String>();
				String[] subTerms = info.split(":")[1].split(";");
				for (int i = 0; i < subTerms.length; i++) {
					set.add(subTerms[i]);
				}
				userInfoList.add(set);
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void gen(boolean flag, String outputPath) {
		String mid = null;
		try {
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));
			long rec = 0;
			int ptr = 0;
			int total = userInfoList.size();
			while (ptr < limit) {
				rec++;
				int ptr1 = (new Random()).nextInt(total);
				int ptr2 = (new Random()).nextInt(total);
				String uid1 = ptrUidMap.get(ptr1);
				String uid2 = ptrUidMap.get(ptr2);
				if ((userInfoList.get(ptr1).contains(uid2) || userInfoList.get(ptr2).contains(uid1)) == flag) {

					HashSet<String> jointSet = new HashSet<String>();
					jointSet.addAll(userInfoList.get(ptr1));
					jointSet.retainAll(userInfoList.get(ptr2));
					if (jointSet.size() == 0) {
						continue;
					}

					HashSet<String> unionSet = new HashSet<String>();
					unionSet.addAll(userInfoList.get(ptr1));
					unionSet.addAll(userInfoList.get(ptr2));

					int kx = userInfoList.get(ptr1).size();
					int ky = userInfoList.get(ptr2).size();
					int numJointSet = jointSet.size();
					int numUnionSet = unionSet.size();

					System.out.println("rec:" + rec);
					System.out.println("ptr:" + ptr);

					out1.write(flag + ";" + uid1 + ";" + uid2 + "|");
					out1.write(kx + ";");// x度数
					out1.write(ky + ";");// y度数
					out1.write(kx + ky + ";");// x度数+y度数
					out1.write(Math.abs(kx - ky) + ";");// |x度数-y度数|
					out1.write(numUnionSet + ";"); // 邻居并集的数量
					out1.write(numJointSet + ";"); // 共同邻居数量

					List<Integer> kList = new ArrayList<Integer>();
					Iterator<String> itr = jointSet.iterator();
					while (itr.hasNext()) {
						mid = itr.next();
						if (!uidPtrMap.containsKey(mid)) {
							continue;
						}
						int k = userInfoList.get(uidPtrMap.get(mid)).size();
						kList.add(k);
					}

					out1.write(calJaccardIndex(numJointSet, numUnionSet) + ";");
					out1.write(calSaltonIndex(numJointSet, kx, ky) + ";");
					out1.write(calPAIndex(kx, ky) + ";");
					out1.write(calAAIndex(kList) + ";");
					out1.write(calRAIndex(kList) + ";");
					out1.write("\n");
					ptr++;
				}
			}
			out1.close();
		} catch (Exception e) {
			System.out.println(mid);
			e.printStackTrace();
		}
	}

	static double calJaccardIndex(int numJointSet, int numUnionSet) {
		return (double) numJointSet / (double) numUnionSet;
	}

	static double calSaltonIndex(int numJointSet, int kx, int ky) {
		return (double) numJointSet / Math.sqrt(kx * ky);
	}

	static double calPAIndex(int kx, int ky) {
		return (double) (kx * ky);
	}

	static double calAAIndex(List<Integer> kList) {
		double score = 0;
		for (int k : kList) {
			score += 1.0 / (Math.log((double) k) / Math.log(2.0));
		}
		return score;
	}

	static double calRAIndex(List<Integer> kList) {
		double score = 0;
		for (int k : kList) {
			score += 1.0 / (double) k;
		}
		return score;
	}
}

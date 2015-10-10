package cn.edu.jlu.zhangc10.kddcup.sna.cal;

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

public class calUsersSimilarity {

	private static String inputpath1 = "data/track1/user_sns_change";
	private static String outputpath1 = "data/track1/user_sns_similarity";
	private static Map<String, Integer> uidPtrMap = new HashMap<String, Integer>();
	private static Map<Integer, String> ptrUidMap = new HashMap<Integer, String>();
	private static List<HashSet<String>> userInfoList = new ArrayList<HashSet<String>>();
	private static double s;
	
	public static void main(String[] args) {
		init();
		cal();
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

	
	static void cal() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			String line;
			int ptr = 0;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String uid = terms[0];
				String info = terms[1];
				String[] subTerms = info.split(":")[1].split(";");
				out1.write(uid + "\t");
				for (int i = 0; i < subTerms.length; i++) {
					s = 0;
					String uid2 = subTerms[i];
					check(uid2);
					if(!ptrUidMap.containsKey(uid)) {
						out1.write(uid2 + ":" + s + ";");
						continue;
					}
					if(!ptrUidMap.containsKey(uid2)) {
						out1.write(uid2 + ":" + s + ";");
						continue;
					}
					int ptr1= Integer.valueOf(ptrUidMap.get(uid));
					int ptr2 = Integer.valueOf(ptrUidMap.get(uid2));

					HashSet<String> jointSet = new HashSet<String>();
					jointSet.addAll(userInfoList.get(ptr1));
					jointSet.retainAll(userInfoList.get(ptr2));
					if (jointSet.size() == 0) {
						out1.write(uid2 + ":" + s + ";");
						continue;
					}

					HashSet<String> unionSet = new HashSet<String>();
					unionSet.addAll(userInfoList.get(ptr1));
					unionSet.addAll(userInfoList.get(ptr2));

					List<Double> featureList = new ArrayList<Double>();

					int kx = userInfoList.get(ptr1).size();
					int ky = userInfoList.get(ptr2).size();
					int kxky1 = kx + ky;
					int kxky2 = Math.abs(kx - ky);
					int numJointSet = jointSet.size();
					int numUnionSet = unionSet.size();

					featureList.add((double) kx);
					featureList.add((double) ky);
					featureList.add((double) kxky1);
					featureList.add((double) kxky2);
					featureList.add((double) numJointSet);
					featureList.add((double) numUnionSet);

					List<Integer> kList = new ArrayList<Integer>();
					Iterator<String> itr = jointSet.iterator();
					while (itr.hasNext()) {
						String mid = itr.next();
						if (!uidPtrMap.containsKey(mid)) {
							continue;
						}
						int k = userInfoList.get(uidPtrMap.get(mid)).size();
						kList.add(k);
					}

					featureList.add(calJaccardIndex(numJointSet, numUnionSet));
					featureList.add(calSaltonIndex(numJointSet, kx, ky));
					featureList.add(calPAIndex(kx, ky));
					//featureList.add(calAAIndex(kList));
					featureList.add(calRAIndex(kList));

					out1.write(uid2 + ":" + Predict.getInstance().cal(featureList) + ";");
				}
				out1.write("\n");
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
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

	static void check(String uid) {
		if(!ptrUidMap.containsKey(uid)) {
			//System.out.println("uid:" + uid + " is not exsit.");
		}
		s = Predict.newInstance();
	}
	
	
	static double calRAIndex(List<Integer> kList) {
		double score = 0;
		for (int k : kList) {
			score += 1.0 / (double) k;
		}
		return score;
	}
}

package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jlu.zhangc10.recsys.preprocess1.GetUserFeaturesPtrList;

public class CalRatings {

	private static String userFeaturesPath = "data/track1/user_features";
	private static String featureTimesPath = "data/track1/features_times";
	private static String itemPtrPath = "data/track1/new_matrix_iid";
	private static String resultMatrixPath = "data/track1/new_matrix_result";
	private static String testPath = "data/track1/original/new_test_sort";
	private static String ratingsPath = "data/track1/new_ratings";

	private static Map<String, String> userFeaturesMap = new HashMap<String, String>();
	private static List<Integer> featureTimesList = new ArrayList<Integer>();
	private static Map<String, Integer> itemPtrMap = new HashMap<String, Integer>();
	private static List<ArrayList<Double>> resultMatrix = new ArrayList<ArrayList<Double>>();

	public static void main(String[] args) {

		System.out.println(new Date() + " loadList ...");
		loadList(featureTimesList, featureTimesPath);

		System.out.println(new Date() + " loadItemPtrMap ...");
		loadItemPtrMap();

		System.out.println(new Date() + " loadUserFeaturesInfo ...");
		loadUserFeaturesInfo();

		System.out.println(new Date() + " loadMatrix ...");
		loadMatrix(resultMatrix, resultMatrixPath);

		System.out.println(new Date() + " predict ...");
		calRatings();
	}

	static void loadList(List<Integer> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line = in1.readLine();
			String[] terms = line.split(" ");
			for (int i = 0; i < terms.length; i++) {
				list.add(Integer.valueOf(terms[i]));
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadItemPtrMap() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(itemPtrPath));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				itemPtrMap.put(line, ptr);
				ptr++;
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadUserFeaturesInfo() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(userFeaturesPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				userFeaturesMap.put(terms[0], terms[1] + "\t" + terms[2] + "\t" + terms[3] + "\t" + terms[4]);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadMatrix(List<ArrayList<Double>> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line;
			while ((line = in1.readLine()) != null) {
				ArrayList<Double> tempList = new ArrayList<Double>();
				String[] terms = line.split(" ");
				for (int i = 0; i < terms.length; i++) {
					// 暂时特殊处理
					if(terms[i].equals("NaN")) {
						terms[i] = "0";
					}
					tempList.add(Double.valueOf(terms[i]));
				}
				list.add(tempList);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void calRatings() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(testPath));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(ratingsPath));
			int numberNoFeatureUsers = 0;
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String userFeatures = "0	0	0	0";
				if (userFeaturesMap.containsKey(user)) {
					userFeatures = userFeaturesMap.get(user);
				} else {
					numberNoFeatureUsers++;
				}
				String[] features = userFeatures.split("\t");
				List<Integer> featurePtrList = GetUserFeaturesPtrList.getFeaturesPtrList(features[0] + "\t"
						+ features[1] + "\t" + features[2] + "\t" + features[3]);
				double score = calScore(item, featurePtrList);
				out1.write(line + "\t" + score + "\n");
				ptr++;
				if (ptr % 1000000 == 0) {
					System.out.println(ptr / 1000000 + "00w");
				}
			}
			System.out.println("numberNoFeatureUsers:" + numberNoFeatureUsers);
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static double calScore(String item, List<Integer> featurePtrList) {
		int ptr = -1;
		if (itemPtrMap.containsKey(item)) {
			ptr = itemPtrMap.get(item);
		}
		if (ptr == -1) {
			return 0;
		}
		double score = 0;
		for (int featurePtr : featurePtrList) {
			double featureWeight = (double) (Math.log((double) 2320895
					/ (double) (featureTimesList.get(featurePtr) + 150)) / Math.log(2.0));
			score += resultMatrix.get(ptr).get(featurePtr) * featureWeight;
		}
		return score;
	}
}

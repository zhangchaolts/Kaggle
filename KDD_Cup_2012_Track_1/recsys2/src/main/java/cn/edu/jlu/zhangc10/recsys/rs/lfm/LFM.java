package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jlu.zhangc10.recsys.rs.preprocess.GetUserFeatureArray;

public class LFM {

	private static String matrixPath = "data/track1/matrix_probability";
	private static List<ArrayList<Float>> matrix = new ArrayList<ArrayList<Float>>();
	private static String resultMatrixPath = "data/track1/matrix_result";
	private static List<ArrayList<Float>> resultMatrix = new ArrayList<ArrayList<Float>>();
	private static String featureWeightInfoPath = "data/track1/user_features";
	private static String featureWeightPath = "data/track1/features_weight";
	private static List<Float> featureWeightList = new ArrayList<Float>();
	private static String itemPtrPath = "data/track1/matrix_iid";
	private static Map<String, Integer> itemPtrMap = new HashMap<String, Integer>();
	private static String userFeaturesPath = "data/track1/user_features";
	private static Map<String, String> userFeaturesMap = new HashMap<String, String>();
	private static String trainPath = "data/track1/train";
	private static double proportion;
	private static String testPath = "data/track1/test";
	private static String result1Path = "data/track1/result1";
	private static String result2Path = "data/track1/result4";

	private static float mu;

	private static final int iteration = 50;
	private static final float alpha = 0.5f;
	private static final float lambda = 0.001f;
	private static final float step = 0.9f;
	private static final int F = 100;

	private static List<Float> biList = new ArrayList<Float>();
	private static List<Float> bfList = new ArrayList<Float>();
	private static List<ArrayList<Float>> pMatrix = new ArrayList<ArrayList<Float>>();
	private static List<ArrayList<Float>> qMatrix = new ArrayList<ArrayList<Float>>();

	public static void main(String[] args) {

		// loadMatrix(matrix, matrixPath);
		//
		// //mu = MuMethod.calMu(matrix);
		// //System.out.println("mu:" + mu);//0.08559624
		// mu = 0.08559624f;
		//
		// System.out.println(new Date() + " LFMMethod.learningBiasLFM ...");
		// LFMMethod.learningBiasLFM(matrix, mu, biList, bfList, pMatrix,
		// qMatrix, F, iteration, alpha, lambda,
		// step);
		//
		// System.out.println(new Date() + " calResultMatrix ...");
		// calResultMatrix();
		//
		// System.out.println(new Date() + " saveMatrix ...");
		// saveMatrix(resultMatrix, resultMatrixPath);

		System.out.println(new Date() + " loadMatrix ...");
		loadMatrix(resultMatrix, resultMatrixPath);
		System.out.println(resultMatrix.size());
		System.out.println(resultMatrix.get(0).size());

		// calProportion();
		// System.out.println(new Date() + " proportion:" +
		// proportion);//0.07177583417314343
		proportion = 0.07177583417314343;

		System.out.println(new Date() + " calFeatureWeightList ...");
		// calFeatureWeightList();
		calFeatureWeightList2();

		System.out.println(new Date() + " saveList ...");
		saveList(featureWeightList, featureWeightPath);

		System.out.println(new Date() + " loadList ...");
		loadList(featureWeightList, featureWeightPath);

		System.out.println(new Date() + " loadItemPtrMap ...");
		loadItemPtrMap();

		System.out.println(new Date() + " loadUserFeaturesInfo ...");
		loadUserFeaturesInfo();

		System.out.println(new Date() + " predict ...");
		// predict();
		// predict2();
		predict3();
	}

	static void calResultMatrix() {
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Float> subList = new ArrayList<Float>();
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0) {
					float pxy = LFMMethod.predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
					subList.add(pxy);
				} else {
					subList.add(matrix.get(i).get(j));
				}
			}
			resultMatrix.add(subList);
		}
	}

	static void saveMatrix(List<ArrayList<Float>> list, String outputPath) {
		try {
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < list.get(i).size(); j++) {
					out1.write(list.get(i).get(j) + " ");
				}
				out1.write("\n");
			}
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadMatrix(List<ArrayList<Float>> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line;
			while ((line = in1.readLine()) != null) {
				ArrayList<Float> tempList = new ArrayList<Float>();
				String[] terms = line.split(" ");
				for (int i = 0; i < terms.length; i++) {
					tempList.add(Float.valueOf(terms[i]));
				}
				list.add(tempList);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void calProportion() {
		long total = 0;
		long positive = 0;
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(trainPath));
			String line;
			while ((line = in1.readLine()) != null) {
				total++;
				String[] terms = line.split("\t");
				if (terms[2].equals("1")) {
					positive++;
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		proportion = (double) positive / (double) total;
	}

	static void calFeatureWeightList() {
		int numberFeatures = 5312;
		int numberUsers = 1392873;
		List<Integer> countList = new ArrayList<Integer>();
		for (int i = 0; i < numberFeatures; i++) {
			countList.add(0);
		}
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(featureWeightInfoPath));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 1000 == 0) {
					System.out.println(ptr / 1000 + "k");
				}
				String[] terms = line.split("\t");
				String userFeatureArray = GetUserFeatureArray.getArray(terms[1] + "\t" + terms[2] + "\t" + terms[3]
						+ "\t" + terms[4]);
				String[] features = userFeatureArray.split(" ");
				for (int i = 0; i < features.length; i++) {
					if (features[i].equals("1")) {
						countList.set(i, countList.get(i) + 1);
					}
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < countList.size(); i++) {
			float weigth = (float) (Math.log((double) numberUsers / (double) (countList.get(i) + 50)) / Math.log(2.0));
			featureWeightList.add(weigth);
		}
	}

	static void calFeatureWeightList2() {
		int numberFeatures = 5312;
		int numberUsers = 1392873;
		List<Integer> countList = new ArrayList<Integer>();
		for (int i = 0; i < numberFeatures; i++) {
			countList.add(0);
		}
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(featureWeightInfoPath));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
				String[] terms = line.split("\t");
				if (terms[2].equals("3")) {
					terms[2] = "0";
				}
				List<Integer> featurePtrList = GetUserFeatureArray.getFeaturePtrList(terms[1] + "\t" + terms[2] + "\t"
						+ terms[3] + "\t" + terms[4]);
				for (int featurePtr : featurePtrList) {
					countList.set(featurePtr, countList.get(featurePtr) + 1);
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < countList.size(); i++) {
			float weigth = (float) (Math.log((double) numberUsers / (double) (countList.get(i) + 50)) / Math.log(2.0));
			featureWeightList.add(weigth);
		}
	}

	static void saveList(List<Float> list, String outputPath) {
		try {
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));
			for (int i = 0; i < list.size(); i++) {
				out1.write(list.get(i) + " ");
			}
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void loadList(List<Float> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line = in1.readLine();
			String[] terms = line.split(" ");
			for (int i = 0; i < terms.length; i++) {
				list.add(Float.valueOf(terms[i]));
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

	static void predict() {
		List<Float> scoreList = new ArrayList<Float>();
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(testPath));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(result1Path));
			int numberNoFeatureUsers = 0;
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 1000 == 0) {
					System.out.println(ptr / 1000 + "k");
				}
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String userFeatures = "0	0	0	0";
				if (userFeaturesMap.containsKey(user)) {
					userFeatures = userFeaturesMap.get(user);
				} else {
					// System.out.println(user + " has no features");
					numberNoFeatureUsers++;
				}
				String userFeatureArray = GetUserFeatureArray.getArray(userFeatures);
				float score = calScore(item, userFeatureArray);
				scoreList.add(score);
				out1.write(line + "\t" + score + "\n");
			}
			System.out.println("numberNoFeatureUsers:" + numberNoFeatureUsers);
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(scoreList);
		float threshold = scoreList.get((int) (proportion * scoreList.size()));

		try {
			BufferedReader in1 = new BufferedReader(new FileReader(result1Path));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(result2Path));
			String line;
			while ((line = in1.readLine()) != null) {
				float score = Float.valueOf(line.split("\t")[3]);
				if (score > threshold) {
					out1.write(line + "\t" + 1 + "\n");
				} else {
					out1.write(line + "\t" + -1 + "\n");
				}
			}
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static float calScore(String item, String userFeatureArray) {
		int ptr = -1;
		if (itemPtrMap.containsKey(item)) {
			ptr = itemPtrMap.get(item);
		}
		if (ptr == -1) {
			return 0;
		}
		String[] features = userFeatureArray.split(" ");
		float score = 0;
		for (int j = 0; j < features.length; j++) {
			if (features[j].equals("1")) {
				score += resultMatrix.get(ptr).get(j) * featureWeightList.get(j);
			}
		}
		return score;
	}

	static float calScore(String item, List<Integer> featurePtrList) {
		int ptr = -1;
		if (itemPtrMap.containsKey(item)) {
			ptr = itemPtrMap.get(item);
		}
		if (ptr == -1) {
			return 0;
		}
		float score = 0;
		for (int featurePtr : featurePtrList) {
			score += resultMatrix.get(ptr).get(featurePtr) * featureWeightList.get(featurePtr);
		}
		return score;
	}

	static void predict2() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader("data/track1/original/rec_log_test.txt"));
			BufferedWriter out1 = new BufferedWriter(new FileWriter("data/track1/result_final"));
			int numberNoFeatureUsers = 0;
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 1000 == 0) {
					System.out.println(ptr / 1000 + "k");
				}
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String userFeatures = "0	0	0	0";
				if (userFeaturesMap.containsKey(user)) {
					userFeatures = userFeaturesMap.get(user);
				} else {
					// System.out.println(user + " has no features");
					numberNoFeatureUsers++;
				}
				String userFeatureArray = GetUserFeatureArray.getArray(userFeatures);
				float score = calScore(item, userFeatureArray);
				out1.write(line + "\t" + score + "\n");
			}
			System.out.println("numberNoFeatureUsers:" + numberNoFeatureUsers);
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void predict3() {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader("data/track1/original/rec_log_test.txt"));
			BufferedWriter out1 = new BufferedWriter(new FileWriter("data/track1/result_final"));
			int numberNoFeatureUsers = 0;
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 10000 == 0) {
					System.out.println(ptr / 10000 + "w");
				}
				String[] terms = line.split("\t");
				String user = terms[0];
				String item = terms[1];
				String userFeatures = "0	0	0	0";
				if (userFeaturesMap.containsKey(user)) {
					userFeatures = userFeaturesMap.get(user);
				} else {
					// System.out.println(user + " has no features");
					numberNoFeatureUsers++;
				}
				String[] features = userFeatures.split("\t");
				if (features[1].equals("3")) {
					features[1] = "0";
				}
				List<Integer> featurePtrList = GetUserFeatureArray.getFeaturePtrList(
				features[0] + "\t" + features[1] + "\t" + features[2] + "\t" + features[3]);
				float score = calScore(item, featurePtrList);
				out1.write(line + "\t" + score + "\n");
			}
			System.out.println("numberNoFeatureUsers:" + numberNoFeatureUsers);
			in1.close();
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

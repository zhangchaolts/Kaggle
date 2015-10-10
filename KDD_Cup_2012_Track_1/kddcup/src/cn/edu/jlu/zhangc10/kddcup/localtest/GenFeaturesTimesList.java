package cn.edu.jlu.zhangc10.kddcup.localtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import cn.edu.jlu.zhangc10.kddcup.preprocess.GetUserFeaturesPtrList;

public class GenFeaturesTimesList {

	private static String inputPath = "data/track1/user_features";
	private static String outputPath = "data/track1/features_times";
	private static List<Integer> featureTimesList = new ArrayList<Integer>();
	private static final int numberFeatures = 5312;

	public static void main(String[] args) {
		calFeatureWeightList();
		saveList(featureTimesList, outputPath);
	}

	static void calFeatureWeightList() {
		for (int i = 0; i < numberFeatures; i++) {
			featureTimesList.add(0);
		}
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				List<Integer> featurePtrList = GetUserFeaturesPtrList.getFeaturesPtrList(terms[1] + "\t" + terms[2] + "\t"
						+ terms[3] + "\t" + terms[4]);
				for (int featurePtr : featurePtrList) {
					featureTimesList.set(featurePtr, featureTimesList.get(featurePtr) + 1);
				}
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

	static void saveList(List<Integer> list, String outputPath) {
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

}

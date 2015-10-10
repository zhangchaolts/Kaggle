package cn.edu.jlu.zhangc10.recsys.sna.cal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class Predict {

	private static String modelPath = "data/track1/J48.model";
	private static String tempCsvFilePath = "data/track1/temp.csv";
	private static Classifier classifier;

	private static Predict instance = null;

	private Predict() {
	}

	public static Predict getInstance() {
		if (instance == null) {
			instance = new Predict();
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
				classifier = (Classifier) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public double cal(List<Double> featureList) {
		double answer = 0;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(tempCsvFilePath));
			for (int i = 1; i <= featureList.size(); i++) {
				out.write("f" + String.valueOf(i) + ",");
			}
			out.write("f" + String.valueOf(featureList.size() + 1) + "\n");
			for (double f : featureList) {
				out.write(f + ",");
			}
			out.write("Yes\n");
			for (double f : featureList) {
				out.write(f + ",");
			}
			out.write("No\n");
			out.close();

			CSVLoader csvLoader = new CSVLoader();
			csvLoader.setFile(new File(tempCsvFilePath));
			Instances instancesTest = csvLoader.getDataSet();
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
			double[] distribute = classifier.distributionForInstance(instancesTest.instance(0));
			
//			for (int j = 0; j < distribute.length; j++) {
//				System.out.print(distribute[j] + ";");
//			}
//			System.out.println();
			
			answer = distribute[0];
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;
	}

	public static void main(String[] args) throws Exception {
		List<Double> featureList = new ArrayList<Double>();
		featureList.add(52d);//1
		featureList.add(554d);//2
		featureList.add(606d);//3
		featureList.add(502d);//4
		featureList.add(594d);//5
		featureList.add(12d);//6
		featureList.add(0.020202020202020204);//7
		featureList.add(0.07070085923669132);//8
		featureList.add(28808.0);//9
		featureList.add(0.9856508625771888);//10
		System.out.println(Predict.getInstance().cal(featureList));
	}
	
	public static double newInstance() {
		return Math.random();
	}
}

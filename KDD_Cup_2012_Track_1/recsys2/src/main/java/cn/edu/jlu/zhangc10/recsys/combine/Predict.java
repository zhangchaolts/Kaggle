package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Predict {

	public static String modelPath = "data/track1/Logistic100W.model";
	public static File inputFile = new File("data/track1/predict.arff");
	public static String outputPath = "data/track1/cb_result";

	public static void main(String[] args) throws Exception {

		ArffLoader arff = new ArffLoader();
		arff.setFile(inputFile);

		Instances instancesTest = arff.getDataSet();
		instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();

		BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));

		double sum = instancesTest.numInstances();
		for (int i = 0; i < sum; i++) {
			if((i+1) % 10000 ==0) {
				System.out.println((i+1)/10000 + "w");
			}
 			double[] distribute = classifier.distributionForInstance(instancesTest.instance(i));
//			for (int j = 0; j < distribute.length; j++) {
//				System.out.print(distribute[j] + ";");
//			}
//			System.out.println();
			out.write(distribute[1] + "\n");
		}
		out.close();
	}
}

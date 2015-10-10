package cn.edu.jlu.zhangc10.kddcup.combine;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

public class StatisticsClassification {

	public static String modelPath = "data/track1/combine_logistc_100w.model";
	public static File inputFile = new File("data/track1/combine_smaples_test.csv");

	private static int actualPositive = 0;
	private static int actualNegative = 0;
	private static int predictPositive = 0;
	private static int predictNegative = 0;
	private static int positiveRight = 0;
	private static int negativeRight = 0;
	private static double sum = 0;

	public static void main(String[] args) throws Exception {

		CSVLoader csvLoader = new CSVLoader();
		csvLoader.setFile(inputFile);

		Instances instancesTest = csvLoader.getDataSet();
		instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();

		for (int i = 0; i < instancesTest.numInstances(); i++) {
			if ((i + 1) % 10000 == 0) {
				System.out.println((i + 1) / 10000 + "w");
			}
			double predict = classifier.classifyInstance(instancesTest.instance(i));
			double actual = instancesTest.instance(i).classValue();
			if (actual == 1.0) {
				actualPositive++;
			} else {
				actualNegative++;
			}
			if (predict == 1.0) {
				predictPositive++;
			} else {
				predictNegative++;
			}
			if (actual == 1.0 && actual == predict) {
				positiveRight++;
			}
			if (actual == 0.0 && actual == predict) {
				negativeRight++;
			}
			if (actual != predict) {
				sum += Math.pow(2.0, 2.0);
			}
		}
		System.out.println(classifier.toString());

		System.out.println("\nAccuracy:" + (double) (positiveRight + negativeRight)
				/ (double) instancesTest.numInstances());
		System.out.println("Rmse:" + Math.sqrt(sum / (double) instancesTest.numInstances()));
	}
}

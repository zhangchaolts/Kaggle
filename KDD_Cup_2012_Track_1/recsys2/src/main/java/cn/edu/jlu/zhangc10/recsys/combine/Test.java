package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVLoader;

public class Test {

	public static String modelPath = "data/track1/J48.model";
	public static File inputFile = new File("data/track1/weka_user_sns_samples.csv");

	public static void main(String[] args) throws Exception {

		CSVLoader arff = new CSVLoader();
		arff.setFile(inputFile);
		
		Instances instancesTest = arff.getDataSet();
		instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelPath));
		Classifier classifier = (Classifier) ois.readObject();
		ois.close();

		double sum = instancesTest.numInstances();
		double right = 0;
		for (int i = 0; i < sum; i++) {
			if(i % 10000 ==0) {
				System.out.println(i/10000 + "w");
			}
//			System.out.println(classifier.classifyInstance(instancesTest.instance(i))
//					+ "|" + instancesTest.instance(i).classValue());
			if (classifier.classifyInstance(instancesTest.instance(i)) == instancesTest.instance(i).classValue()) {
				right++;
			}
			double[] distribute = classifier.distributionForInstance(instancesTest.instance(i));
			for (int j = 0; j < distribute.length; j++) {
				System.out.print(distribute[j] + ";");
			}
			System.out.println();
		}
		
		//System.out.println(classifier.toString());
		System.out.println("precision:" + (right / sum));
	}
}

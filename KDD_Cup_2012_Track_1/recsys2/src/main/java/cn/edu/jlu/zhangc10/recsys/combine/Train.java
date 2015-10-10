package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.File;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class Train {

	// 训练数据
	public static File inputFile = new File("data/track1/combine_result2.arff");
	// 模型保存的路径
	public static String outputPath = "data/track1/NaiveBayes.model";
	// 分类器
	public static Classifier classifier = new NaiveBayes();

	public static void main(String[] args) throws Exception {
		// 读入文件
		ArffLoader arff = new ArffLoader();
		arff.setFile(inputFile);
		
		//获取实例并指明预测信息位置
		Instances instancesTrain = arff.getDataSet();
		instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);

		//训练模型
		classifier.buildClassifier(instancesTrain);

		//保持模型
		SerializationHelper.write(outputPath, classifier);

		System.out.println("Train Process Complete!");
	}
}

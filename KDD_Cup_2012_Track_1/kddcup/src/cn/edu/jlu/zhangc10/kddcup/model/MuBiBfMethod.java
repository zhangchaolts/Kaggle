package cn.edu.jlu.zhangc10.kddcup.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuBiBfMethod {

	private static final int lambda2_limit = 1;
	private static final int lambda3_limit = 9;

	static void process(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu) {
		System.out.println(new Date() + ": start MuBiBfMethod...");
		List<Double> biList = null;
		List<Double> bfList = null;
		double minRmse = 100000;
		int recLambda2 = 0;
		int recLambda3 = 0;
		for (int lambda2 = 1; lambda2 <= lambda2_limit; lambda2 += 2) {
			biList = new ArrayList<Double>();
			calBiList(biList, matrix, lambda2, mu);// item
			for (int lambda3 = 9; lambda3 <= lambda3_limit; lambda3 += 2) {
				bfList = new ArrayList<Double>();
				calBfList(bfList, biList, matrix, lambda3, mu);// feature
				double rmse = calMuBiBfRmse(matrix, originalMatrix, mu, biList, bfList);
				System.out.println("[" + lambda2 + ", " + lambda3 + "]: " + rmse);
				if (rmse < minRmse) {
					minRmse = rmse;
					recLambda2 = lambda2;
					recLambda3 = lambda3;
					CompareAllMethod.setBiList(biList);
					CompareAllMethod.setBfList(bfList);
				}
			}
		}
		System.out.println("mu+bi+bf Rmse:\t" + minRmse + " [" + recLambda2 + "," + recLambda3 + "]");
		//[1, 9]: 0.03945533281148814
	}

	static void calBiList(List<Double> biList, List<ArrayList<Double>> matrix, int lambda2, double mu) {
		for (int i = 0; i < matrix.size(); i++) {
			double sum = 0;
			double valid = 0;
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j) - mu;
					valid += 1.0f;
				}
			}
			if ((double) lambda2 + valid != 0f) {
				biList.add(sum / ((double) lambda2 + valid));
			} else {
				biList.add(0.0);
			}
		}
	}

	static void calBfList(List<Double> bfList, List<Double> biList, List<ArrayList<Double>> matrix, int lambda3,
			double mu) {
		for (int j = 0; j < matrix.get(0).size(); j++) {
			double sum = 0;
			double valid = 0;
			for (int i = 0; i < matrix.size(); i++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j) - mu - biList.get(i);
					valid += 1.0;
				}
			}
			if (((double) lambda3 + valid) != 0f) {
				bfList.add(sum / ((double) lambda3 + valid));
			} else {
				bfList.add(0.0);
			}
		}
	}

	static double calMuBiBfRmse(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu,
			List<Double> biList, List<Double> bfList) {
		double sum = 0;
		double valid = 0;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((mu + biList.get(i) + bfList.get(j) - originalMatrix.get(i).get(j)), 2.0);
					valid += 1.0;
				}
			}
		}
		double rmse = 0;
		if (valid != 0) {
			rmse = (double) Math.sqrt(sum / valid);
		}
		return rmse;
	}

}

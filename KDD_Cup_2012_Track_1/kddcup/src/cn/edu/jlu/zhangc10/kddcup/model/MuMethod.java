package cn.edu.jlu.zhangc10.kddcup.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuMethod {

	static void process(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix) {
		System.out.println(new Date() + ": start MuMethod...");
		double mu = calMu(matrix);
		CompareAllMethod.setMu(mu);
		double muRmse = MuMethod.calMuRmse(matrix, originalMatrix, mu);
		System.out.println("mu: " + mu);// 0.08662859
		System.out.println("mu Rmse: " + muRmse);// 0.05677143
	}

	public static double calMu(List<ArrayList<Double>> matrix) {
		double sum = 0;
		double valid = 0;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j);
					valid += 1.0;
				}
			}
		}
		double mu = 0;
		if (valid != 0) {
			mu = sum / valid;
		}
		return mu;
	}

	static double calMuRmse(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, Double mu) {
		double sum = 0;
		double valid = 0;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((mu - originalMatrix.get(i).get(j)), 2.0);
					valid += 1.0;
				}
			}
		}
		double muRmse = 0;
		if (valid != 0) {
			muRmse = (double) Math.sqrt(sum / valid);
		}
		return muRmse;
	}
}

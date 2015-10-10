package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuMethod {

	static void process(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix) {
		System.out.println(new Date() + ": start MuMethod...");
		float mu = calMu(matrix);
		CompareAllMethod.setMu(mu);
		float muRmse = MuMethod.calMuRmse(matrix, originalMatrix, mu);
		System.out.println("mu: " + mu);// 0.0855527
		System.out.println("mu Rmse: " + muRmse);// 0.055682078
	}

	static float calMu(List<ArrayList<Float>> matrix) {
		float sum = 0f;
		float valid = 0f;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j);
					valid += 1.0;
				}
			}
		}
		float mu = 0f;
		if (valid != 0) {
			mu = sum / valid;
		}

		return mu;
	}

	static float calMuRmse(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, Float mu) {
		float sum = 0f;
		float valid = 0f;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((mu - originalMatrix.get(i).get(j)), 2.0);
					valid += 1.0;
				}
			}
		}
		float muRmse = 0f;
		if (valid != 0) {
			muRmse = (float) Math.sqrt(sum / valid);
		}
		return muRmse;
	}
}

package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuBiBfMethod {

	private static final int lambda2_limit = 2;
	private static final int lambda3_limit = 8;

	static void process(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu) {
		System.out.println(new Date() + ": start MuBiBfMethod...");
		List<Float> biList = null;
		List<Float> bfList = null;
		float minRmse = 100000f;
		int recLambda2 = 0;
		int recLambda3 = 0;
		for (int lambda2 = 1; lambda2 < lambda2_limit; lambda2 += 2) {
			biList = new ArrayList<Float>();
			calBiList(biList, matrix, lambda2, mu);// item
			for (int lambda3 = 7; lambda3 < lambda3_limit; lambda3 += 2) {
				bfList = new ArrayList<Float>();
				calBfList(bfList, biList, matrix, lambda3, mu);// feature
				float rmse = calMuBiBfRmse(matrix, originalMatrix, mu, biList, bfList);
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
		// 0.039072968 [1,7]
	}

	static void calBiList(List<Float> biList, List<ArrayList<Float>> matrix, int lambda2, float mu) {
		for (int i = 0; i < matrix.size(); i++) {
			float sum = 0f;
			float valid = 0f;
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j) - mu;
					valid += 1.0f;
				}
			}
			if ((float) lambda2 + valid != 0f) {
				biList.add(sum / ((float) lambda2 + valid));
			} else {
				biList.add(0.0f);
			}
		}
	}

	static void calBfList(List<Float> bfList, List<Float> biList, List<ArrayList<Float>> matrix, int lambda3, float mu) {
		for (int j = 0; j < matrix.get(0).size(); j++) {
			float sum = 0f;
			float valid = 0f;
			for (int i = 0; i < matrix.size(); i++) {
				if (matrix.get(i).get(j) != 0) {
					sum += matrix.get(i).get(j) - mu - biList.get(i);
					valid += 1.0;
				}
			}
			if (((float) lambda3 + valid) != 0f) {
				bfList.add(sum / ((float) lambda3 + valid));
			} else {
				bfList.add(0.0f);
			}
		}
	}

	static Float calMuBiBfRmse(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu,
			List<Float> biList, List<Float> bfList) {
		float sum = 0f;
		float valid = 0f;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((mu + biList.get(i) + bfList.get(j) - originalMatrix.get(i).get(j)), 2.0);
					valid += 1.0;
				}
			}
		}
		float rmse = 0f;
		if (valid != 0) {
			rmse = (float) Math.sqrt(sum / valid);
		}
		return rmse;
	}

}

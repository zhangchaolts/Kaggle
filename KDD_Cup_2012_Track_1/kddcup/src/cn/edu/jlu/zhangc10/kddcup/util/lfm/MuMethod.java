package cn.edu.jlu.zhangc10.kddcup.util.lfm;

import java.util.ArrayList;
import java.util.List;

public class MuMethod {

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

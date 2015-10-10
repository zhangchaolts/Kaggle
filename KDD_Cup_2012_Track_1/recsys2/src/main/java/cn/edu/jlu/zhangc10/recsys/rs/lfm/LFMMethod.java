package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LFMMethod {

	private static int iteration = 50;
	private static float alpha = 0.05f;
	private static float lambda = 0.05f;
	private static float step = 0.9f;

	static void setArgs(int iteration1, float alpha1, float lambda1, float step1) {
		iteration = iteration1;
		alpha = alpha1;
		lambda = lambda1;
		step = step1;
	}

	static void process(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu) {
		System.out.println(new Date() + ": start LFMMethod...");
		float minLFMRmse = 100000.0f;
		int bestF = 0;
		for (int F = 310; F <= 310; F += 10) {
			List<Float> biList = new ArrayList<Float>();
			List<Float> bfList = new ArrayList<Float>();
			List<ArrayList<Float>> pMatrix = new ArrayList<ArrayList<Float>>();
			List<ArrayList<Float>> qMatrix = new ArrayList<ArrayList<Float>>();
			learningBiasLFM(matrix, mu, biList, bfList, pMatrix, qMatrix, F, iteration, alpha, lambda, step);
			float rmse = calBiasLFMRmse(matrix, originalMatrix, mu, biList, bfList, pMatrix, qMatrix, F);
			System.out.println("[" + F + "]: " + rmse);
			if (rmse < minLFMRmse) {
				minLFMRmse = rmse;
				bestF = F;
			}
		}
		System.out.println("mu+bi+bf Rmse:\t" + minLFMRmse + " [" + bestF + "]");
	}

	static void learningBiasLFM(List<ArrayList<Float>> matrix, float mu, List<Float> biList, List<Float> bfList,
			List<ArrayList<Float>> pMatrix, List<ArrayList<Float>> qMatrix, int F, int iteration, float alpha,
			float lambda, float step) {
		initBiasLFM(matrix, biList, bfList, pMatrix, qMatrix, F);
		for (int iter = 0; iter < iteration; iter++) {
			for (int i = 0; i < matrix.size(); i++) {
				for (int j = 0; j < matrix.get(0).size(); j++) {
					if (matrix.get(i).get(j) != 0f) {
						float pij = predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
						float eij = matrix.get(i).get(j) - pij;
						float temp1 = biList.get(i) + alpha * (eij - lambda * biList.get(i));
						biList.set(i, temp1);
						float temp2 = bfList.get(j) + alpha * (eij - lambda * bfList.get(j));
						bfList.set(j, temp2);
						for (int f = 0; f < F; f++) {
							float temp3 = pMatrix.get(i).get(f) + alpha
									* (qMatrix.get(j).get(f) * eij - lambda * pMatrix.get(i).get(f));
							pMatrix.get(i).set(f, temp3);
							float temp4 = qMatrix.get(j).get(f) + alpha
									* (pMatrix.get(i).get(f) * eij - lambda * qMatrix.get(j).get(f));
							qMatrix.get(j).set(f, temp4);
						}
					}
				}
			}
			alpha *= step;
		}
	}

	static void initBiasLFM(List<ArrayList<Float>> matrix, List<Float> biList, List<Float> bfList,
			List<ArrayList<Float>> pMatrix, List<ArrayList<Float>> qMatrix, int F) {
		for (int i = 0; i < matrix.size(); i++) {
			biList.add(0.0f);
		}
		for (int j = 0; j < matrix.get(0).size(); j++) {
			bfList.add(0.0f);
		}
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Float> tempList = new ArrayList<Float>();
			for (int f = 0; f < F; f++) {
				tempList.add((float) (Math.random() / Math.sqrt((double) F)));
			}
			pMatrix.add(tempList);
		}
		for (int j = 0; j < matrix.get(0).size(); j++) {
			ArrayList<Float> tempList = new ArrayList<Float>();
			for (int f = 0; f < F; f++) {
				tempList.add((float) (Math.random() / Math.sqrt((double) F)));
			}
			qMatrix.add(tempList);
		}
	}

	static float predict(int i, int j, float mu, List<Float> biList, List<Float> bfList,
			List<ArrayList<Float>> pMatrix, List<ArrayList<Float>> qMatrix, int F) {
		float ret = mu + biList.get(i) + bfList.get(j);
		for (int f = 0; f < F; f++) {
			ret += pMatrix.get(i).get(f) * qMatrix.get(j).get(f);
		}
		return ret;
	}

	static float calBiasLFMRmse(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu,
			List<Float> biList, List<Float> bfList, List<ArrayList<Float>> pMatrix, List<ArrayList<Float>> qMatrix,
			int F) {
		float sum = 0f;
		float valid = 0f;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					float pxy = predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
					sum += Math.pow((pxy - originalMatrix.get(i).get(j)), 2.0);
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

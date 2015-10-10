package cn.edu.jlu.zhangc10.recsys.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LFMMethod {

	private static int iteration = 50;
	private static double alpha = 0.05;
	private static double lambda = 0.05;
	private static double step = 0.9;

	static void setArgs(int iteration1, double alpha1, double lambda1, double step1) {
		iteration = iteration1;
		alpha = alpha1;
		lambda = lambda1;
		step = step1;
	}

	static void process(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu) {
		System.out.println(new Date() + ": start LFMMethod...");
		double minLFMRmse = 100000.0;
		int bestF = 0;
		for (int F = 310; F <= 310; F += 50) {
			List<Double> biList = new ArrayList<Double>();
			List<Double> bfList = new ArrayList<Double>();
			List<ArrayList<Double>> pMatrix = new ArrayList<ArrayList<Double>>();
			List<ArrayList<Double>> qMatrix = new ArrayList<ArrayList<Double>>();
			learningBiasLFM(matrix, mu, biList, bfList, pMatrix, qMatrix, F, iteration, alpha, lambda, step);
			double rmse = calBiasLFMRmse(matrix, originalMatrix, mu, biList, bfList, pMatrix, qMatrix, F);
			System.out.println("[" + iteration + "," + alpha + "," + lambda + "," + step + "," + F + "]: " + rmse);
			//[30,0.5,0.0010,0.9,310]: 0.03232531898471508
			if (rmse < minLFMRmse) {
				minLFMRmse = rmse;
				bestF = F;
			}
		}
		System.out.println("mu+bi+bf Rmse:\t" + minLFMRmse + " [" + bestF + "]");
	}

	public static void learningBiasLFM(List<ArrayList<Double>> matrix, double mu, List<Double> biList,
			List<Double> bfList, List<ArrayList<Double>> pMatrix, List<ArrayList<Double>> qMatrix, int F,
			int iteration, double alpha, double lambda, double step) {
		initBiasLFM(matrix, biList, bfList, pMatrix, qMatrix, F);
		for (int iter = 0; iter < iteration; iter++) {
			for (int i = 0; i < matrix.size(); i++) {
				for (int j = 0; j < matrix.get(0).size(); j++) {
					if (matrix.get(i).get(j) != 0) {
						double pij = predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
						double eij = matrix.get(i).get(j) - pij;
						double temp1 = biList.get(i) + alpha * (eij - lambda * biList.get(i));
//						if (Double.isNaN(temp1)) {
//							temp1 = 0;
//						}
						biList.set(i, temp1);
						double temp2 = bfList.get(j) + alpha * (eij - lambda * bfList.get(j));
//						if (Double.isNaN(temp2)) {
//							temp2 = 0;
//						}
						bfList.set(j, temp2);
						for (int f = 0; f < F; f++) {
							double temp3 = pMatrix.get(i).get(f) + alpha
									* (qMatrix.get(j).get(f) * eij - lambda * pMatrix.get(i).get(f));
//							if (Double.isNaN(temp3)) {
//								temp3 = 0;
//							}
							pMatrix.get(i).set(f, temp3);
							double temp4 = qMatrix.get(j).get(f) + alpha
									* (pMatrix.get(i).get(f) * eij - lambda * qMatrix.get(j).get(f));
//							if (Double.isNaN(temp4)) {
//								temp4 = 0;
//							}
							qMatrix.get(j).set(f, temp4);
						}
					}
				}
			}
			alpha *= step;
		}
	}

	static void initBiasLFM(List<ArrayList<Double>> matrix, List<Double> biList, List<Double> bfList,
			List<ArrayList<Double>> pMatrix, List<ArrayList<Double>> qMatrix, int F) {
		for (int i = 0; i < matrix.size(); i++) {
			biList.add(0.0);
		}
		for (int j = 0; j < matrix.get(0).size(); j++) {
			bfList.add(0.0);
		}
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Double> tempList = new ArrayList<Double>();
			for (int f = 0; f < F; f++) {
				tempList.add((double) (Math.random() / Math.sqrt((double) F)));
			}
			pMatrix.add(tempList);
		}
		for (int j = 0; j < matrix.get(0).size(); j++) {
			ArrayList<Double> tempList = new ArrayList<Double>();
			for (int f = 0; f < F; f++) {
				tempList.add((double) (Math.random() / Math.sqrt((double) F)));
			}
			qMatrix.add(tempList);
		}
	}

	public static double predict(int i, int j, double mu, List<Double> biList, List<Double> bfList,
			List<ArrayList<Double>> pMatrix, List<ArrayList<Double>> qMatrix, int F) {
		double ret = mu + biList.get(i) + bfList.get(j);
		for (int f = 0; f < F; f++) {
			ret += pMatrix.get(i).get(f) * qMatrix.get(j).get(f);
		}
//		if (Double.isNaN(ret)) {
//			ret = 0;
//		}
		return ret;
	}

	static double calBiasLFMRmse(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu,
			List<Double> biList, List<Double> bfList, List<ArrayList<Double>> pMatrix, List<ArrayList<Double>> qMatrix,
			int F) {
		double sum = 0;
		double valid = 0;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					double pxy = predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
					double temp = Math.pow((pxy - originalMatrix.get(i).get(j)), 2.0);
//					if (Double.isInfinite(temp)) {
//						temp = 0;
//					}
					sum += temp;
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

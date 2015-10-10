package cn.edu.jlu.zhangc10.kddcup.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompareAllMethod {

	private static String matrixPath = "data/track1/matrix_probability_train";
	private static String originalMatrixPath = "data/track1/matrix_probability";
	private static List<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
	private static List<ArrayList<Double>> originalMatrix = new ArrayList<ArrayList<Double>>();

	private static double mu;
	private static List<Double> biList;
	private static List<Double> bfList;

	public static void main(String[] args) throws Exception {

		readMatrix();

		// MuMethod.process(matrix, originalMatrix);
		mu = MuMethod.calMu(matrix);

//		LFMMethod.setArgs(50, 0.5, 0.001, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
//		
//		LFMMethod.setArgs(100, 0.5, 0.001, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
//		
//		LFMMethod.setArgs(100, 0.75, 0.001, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
//		
//		LFMMethod.setArgs(100, 0.10, 0.001, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
//		
//		LFMMethod.setArgs(100, 0.5, 0.0075, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
//		
//		LFMMethod.setArgs(100, 0.5, 0.0005, 0.90);
//		LFMMethod.process(matrix, originalMatrix, mu);
		
		MuBiBfMethod.process(matrix, originalMatrix, mu);
		SVDMethod.process(matrix, originalMatrix, mu, biList, bfList);
	}

	static void readMatrix() {
		System.out.println(new Date() + ": start readMatrix() ...");
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(matrixPath));
			BufferedReader in2 = new BufferedReader(new FileReader(originalMatrixPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split(" ");
				ArrayList<Double> tempList = new ArrayList<Double>();
				for (int i = 0; i < terms.length; i++) {
					Double tempValue = Double.valueOf(terms[i]);
					tempList.add(tempValue);
				}
				matrix.add(tempList);
			}
			in1.close();
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split(" ");
				ArrayList<Double> tempList = new ArrayList<Double>();
				for (int i = 0; i < terms.length; i++) {
					Double tempValue = Double.valueOf(terms[i]);
					tempList.add(tempValue);
				}
				originalMatrix.add(tempList);
			}
			in2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Double getMu() {
		return mu;
	}

	public static void setMu(Double mu) {
		CompareAllMethod.mu = mu;
	}

	public static List<Double> getBiList() {
		return biList;
	}

	public static void setBiList(List<Double> biList) {
		CompareAllMethod.biList = biList;
	}

	public static List<Double> getBfList() {
		return bfList;
	}

	public static void setBfList(List<Double> bfList) {
		CompareAllMethod.bfList = bfList;
	}
}

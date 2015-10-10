package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompareAllMethod {

	private static String matrixPath = "data/track1/matrix_probability_train_temp";////
	private static String originalMatrixPath = "data/track1/matrix_probability_temp";////
	private static List<ArrayList<Float>> matrix = new ArrayList<ArrayList<Float>>();
	private static List<ArrayList<Float>> originalMatrix = new ArrayList<ArrayList<Float>>();

	private static float mu;
	private static List<Float> biList;
	private static List<Float> bfList;

	public static void main(String[] args) throws Exception {

		readMatrix();

		//setMu(0.0855527f);
		setMu(0.08662859f);////
		LFMMethod.setArgs(50, 0.5f, 0.001f, 0.9f);
		LFMMethod.process(matrix, originalMatrix, mu);
		
		//MuMethod.process(matrix, originalMatrix);

		//MuBiBfMethod.process(matrix, originalMatrix, mu);

		//SVDMethod.process(matrix, originalMatrix, mu, biList, bfList);
	}

	static void readMatrix() {
		System.out.println(new Date() + ": start readMatrix() ...");
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(matrixPath));
			BufferedReader in2 = new BufferedReader(new FileReader(originalMatrixPath));
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split(" ");
				ArrayList<Float> tempList = new ArrayList<Float>();
				for (int i = 0; i < terms.length; i++) {
					Float tempValue = Float.valueOf(terms[i]);
					tempList.add(tempValue);
				}
				matrix.add(tempList);
			}
			in1.close();
			while ((line = in2.readLine()) != null) {
				String[] terms = line.split(" ");
				ArrayList<Float> tempList = new ArrayList<Float>();
				for (int i = 0; i < terms.length; i++) {
					Float tempValue = Float.valueOf(terms[i]);
					tempList.add(tempValue);
				}
				originalMatrix.add(tempList);
			}
			in2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Float getMu() {
		return mu;
	}

	public static void setMu(Float mu) {
		CompareAllMethod.mu = mu;
	}

	public static List<Float> getBiList() {
		return biList;
	}

	public static void setBiList(List<Float> biList) {
		CompareAllMethod.biList = biList;
	}

	public static List<Float> getBfList() {
		return bfList;
	}

	public static void setBfList(List<Float> bfList) {
		CompareAllMethod.bfList = bfList;
	}
}

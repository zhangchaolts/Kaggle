package cn.edu.jlu.zhangc10.kddcup.util.lfm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LFM {
	private static String matrixPath = "data/track1/submission_matrix_probability";
	private static String resultMatrixPath = "data/track1/submission_matrix_result_compare";

	private static List<ArrayList<Float>> matrix = new ArrayList<ArrayList<Float>>();
	private static List<ArrayList<Float>> resultMatrix = new ArrayList<ArrayList<Float>>();

	private static final int iteration = 200;
	private static final float alpha = 0.5f;
	private static final float lambda = 0.001f;
	private static final float step = 0.90f;
	private static final int F = 310;

	private static float mu;
	private static List<Float> biList = new ArrayList<Float>();
	private static List<Float> bfList = new ArrayList<Float>();
	private static List<ArrayList<Float>> pMatrix = new ArrayList<ArrayList<Float>>();
	private static List<ArrayList<Float>> qMatrix = new ArrayList<ArrayList<Float>>();

	public static void main(String[] args) {
		
		System.out.println(new Date() + " loadMatrix ...");
		loadMatrix(matrix, matrixPath);
		
		System.out.println(new Date() + " MuMethod.calMu ...");
		mu = MuMethod.calMu(matrix);

		System.out.println(new Date() + " LFMMethod.learningBiasLFM ...");
		LFMMethod.learningBiasLFM(matrix, mu, biList, bfList, pMatrix, qMatrix, F, iteration, alpha, lambda, step);

		System.out.println(new Date() + " calResultMatrix ...");
		calResultMatrix();

		System.out.println(new Date() + " saveMatrix ...");
		saveMatrix(resultMatrix, resultMatrixPath);
	}

	static void loadMatrix(List<ArrayList<Float>> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line;
			while ((line = in1.readLine()) != null) {
				ArrayList<Float> tempList = new ArrayList<Float>();
				String[] terms = line.split(" ");
				for (int i = 0; i < terms.length; i++) {
					tempList.add(Float.valueOf(terms[i]));
				}
				list.add(tempList);
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void calResultMatrix() {
		for (int i = 0; i < matrix.size(); i++) {
			ArrayList<Float> subList = new ArrayList<Float>();
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0) {
					float pxy = LFMMethod.predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
					subList.add(pxy);
				} else {
					subList.add(matrix.get(i).get(j));
				}
			}
			resultMatrix.add(subList);
		}
	}

	static void saveMatrix(List<ArrayList<Float>> matrix, String outputPath) {
		try {
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputPath));
			for (int i = 0; i < matrix.size(); i++) {
				for (int j = 0; j < matrix.get(i).size(); j++) {
					out1.write(matrix.get(i).get(j) + " ");
				}
				out1.write("\n");
			}
			out1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

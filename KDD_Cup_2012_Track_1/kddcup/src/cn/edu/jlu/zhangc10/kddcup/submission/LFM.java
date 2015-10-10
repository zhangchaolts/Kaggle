package cn.edu.jlu.zhangc10.kddcup.submission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import cn.edu.jlu.zhangc10.kddcup.model.LFMMethod;
import cn.edu.jlu.zhangc10.kddcup.model.MuMethod;


public class LFM {
	private static String matrixPath = "data/track1/submission_matrix_probability";
	private static String resultMatrixPath = "data/track1/submission_matrix_result";

	private static List<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
	private static List<ArrayList<Double>> resultMatrix = new ArrayList<ArrayList<Double>>();

	private static final int iteration = 500;
	private static final double alpha = 0.5;
	private static final double lambda = 0.001;
	private static final double step = 0.90;
	private static final int F = 510;

	private static double mu;
	private static List<Double> biList = new ArrayList<Double>();
	private static List<Double> bfList = new ArrayList<Double>();
	private static List<ArrayList<Double>> pMatrix = new ArrayList<ArrayList<Double>>();
	private static List<ArrayList<Double>> qMatrix = new ArrayList<ArrayList<Double>>();

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

	static void loadMatrix(List<ArrayList<Double>> list, String inputPath) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputPath));
			String line;
			while ((line = in1.readLine()) != null) {
				ArrayList<Double> tempList = new ArrayList<Double>();
				String[] terms = line.split(" ");
				for (int i = 0; i < terms.length; i++) {
					tempList.add(Double.valueOf(terms[i]));
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
			ArrayList<Double> subList = new ArrayList<Double>();
			for (int j = 0; j < matrix.get(i).size(); j++) {
				if (matrix.get(i).get(j) == 0) {
					double pxy = LFMMethod.predict(i, j, mu, biList, bfList, pMatrix, qMatrix, F);
					subList.add(pxy);
				} else {
					subList.add(matrix.get(i).get(j));
				}
			}
			resultMatrix.add(subList);
		}
	}

	static void saveMatrix(List<ArrayList<Double>> matrix, String outputPath) {
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

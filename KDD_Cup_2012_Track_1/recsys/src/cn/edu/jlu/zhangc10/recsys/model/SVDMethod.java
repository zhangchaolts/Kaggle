package cn.edu.jlu.zhangc10.recsys.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SingularValueDecomposition;
import org.apache.commons.math.linear.SingularValueDecompositionImpl;

public class SVDMethod {

	private static final double constants = 1.0;

	public static void main(String[] args) {
		double[][] _M = { { 0.4, 3.0 }, { 2.3, 5.3 }, { 2.0, 2.0 }, { 2.0, 5.0 } };
		RealMatrix _R = MatrixUtils.createRealMatrix(_M);
		SingularValueDecomposition SVD = new SingularValueDecompositionImpl(_R);
		RealMatrix _U = SVD.getU();
		RealMatrix _S = SVD.getS();
		RealMatrix _VT = SVD.getVT();
		RealMatrix _RR = _U.multiply(_S).multiply(_VT);
		outputMatrix(_R, "_R");
		outputMatrix(_U, "_U");
		outputMatrix(_S, "_S");
		outputMatrix(_VT, "_VT");
		outputMatrix(_RR, "_RR");
		RealMatrix temp = loadMatrix("_RR");
		System.out.println(temp);
	}

	static void process(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu,
			List<Double> biList, List<Double> bfList) {
		System.out.println(new Date() + ": start SVDMethod...");
		pureSVDEachRank(matrix, originalMatrix, mu, biList, bfList);
	}

	static void pureSVDEachRank(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix, double mu,
			List<Double> buList, List<Double> biList) {

		//RealMatrix R = null;
		RealMatrix U = null;
		RealMatrix S = null;
		RealMatrix VT = null;

		// int row = matrix.size();
		// int col = matrix.get(0).size();
		// double[][] M = new double[row][col];
		// for (int i = 0; i < row; i++) {
		// for (int j = 0; j < col; j++) {
		// if (matrix.get(i).get(j) != 0f) {
		// M[i][j] = matrix.get(i).get(j) * constants;
		// } else {
		// M[i][j] = (mu + buList.get(i) + biList.get(j)) * constants + Math.random() * 0.01 * mu;
		// }
		// }
		// }
		//
		// R = MatrixUtils.createRealMatrix(M);
		// System.out.println(new Date() + ":" + row + "," + col);
		// SingularValueDecomposition SVD = new SingularValueDecompositionImpl(R);
		// U = SVD.getU();
		// S = SVD.getS();
		// VT = SVD.getVT();
		// System.out.println(new Date() + ":" + row + "," + col);
		// outputMatrix(R, "R");
		// outputMatrix(U, "U");
		// outputMatrix(S, "S");
		// outputMatrix(VT, "VT");

		U = loadMatrix("U");
		S = loadMatrix("S");
		VT = loadMatrix("VT");

		int numberLambda = 0;
		for (int i = 0; i < S.getRowDimension(); i++) {
			if (S.getEntry(i, i) == 0) {
				numberLambda = i;
				break;
			}
		}
		if (numberLambda == 0) {
			numberLambda = S.getRowDimension();
		}
		System.out.println("numberLambda: " + numberLambda);
		numberLambda = 101;//////
		double minSVDRmse = 100000.0;
		int bestNumberLambda = 0;
		RealMatrix SS = MatrixUtils.createRealMatrix(S.getData());
		outputMatrix(SS, "SS");
		for (int i = numberLambda - 1; i >= 0; i -= 4) {
			for (int j = /*numberLambda - 1*/4701; j >= i; j--) {
				SS.setEntry(j, j, 0.0);
			}
			RealMatrix RR = U.multiply(SS).multiply(VT);
			outputMatrix(RR, "RR_" + String.valueOf(i));
			double rmse = calSVDRmse(matrix, originalMatrix, RR);
			System.out.println(new Date() + "[" + i + "]: " + rmse);
			//[40]: 0.03427971476292423
			if (rmse < minSVDRmse) {
				minSVDRmse = rmse;
				bestNumberLambda = i;
			}
		}
		System.out.println("pureSVDEachRank Rmse: " + minSVDRmse + "[" + bestNumberLambda + "]");
	}

	static double calSVDRmse(List<ArrayList<Double>> matrix, List<ArrayList<Double>> originalMatrix,
			RealMatrix newRealMatrix) {
		double sum = 0;
		double valid = 0;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((newRealMatrix.getEntry(i, j) / constants - originalMatrix.get(i).get(j)), 2.0);
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

	static void outputMatrix(RealMatrix realMatrix, String outputName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("data/track1/" + outputName));
			for (int i = 0; i < realMatrix.getRowDimension(); i++) {
				for (int j = 0; j < realMatrix.getColumnDimension(); j++) {
					out.write(realMatrix.getEntry(i, j) + " ");
				}
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static RealMatrix loadMatrix(String inputName) {
		RealMatrix realMatrix = null;
		try {
			List<ArrayList<Double>> matrix = new ArrayList<ArrayList<Double>>();
			BufferedReader in = new BufferedReader(new FileReader("data/track1/" + inputName));
			String line;
			while ((line = in.readLine()) != null) {
				String[] terms = line.split(" ");
				ArrayList<Double> tempList = new ArrayList<Double>();
				for (int i = 0; i < terms.length; i++) {
					tempList.add(Double.valueOf(terms[i]));
				}
				matrix.add(tempList);
			}
			in.close();

			double[][] R = new double[matrix.size()][matrix.get(0).size()];
			for (int i = 0; i < matrix.size(); i++) {
				for (int j = 0; j < matrix.get(i).size(); j++) {
					R[i][j] = matrix.get(i).get(j);
				}
			}

			realMatrix = MatrixUtils.createRealMatrix(R);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return realMatrix;
	}
}

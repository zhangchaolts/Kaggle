package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import java.io.BufferedWriter;
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

	private static final float constants = 1.0f;

	static void process(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu,
			List<Float> biList, List<Float> bfList) {
		System.out.println(new Date() + ": start SVDMethod...");
		//pureSVDFullRank(matrix, originalMatrix, mu, biList, bfList);
		pureSVDEachRank(matrix, originalMatrix, mu, biList, bfList);
	}

	static void pureSVDFullRank(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu,
			List<Float> buList, List<Float> biList) {
		int row = matrix.size();
		int col = matrix.get(0).size();
		double[][] M = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (matrix.get(i).get(j) != 0f) {
					M[i][j] = matrix.get(i).get(j) * constants;
				} else {
					M[i][j] = (mu + buList.get(i) + biList.get(j)) * constants;
				}
			}
		}

		RealMatrix realMatrix = MatrixUtils.createRealMatrix(M);
		SingularValueDecomposition SVD = new SingularValueDecompositionImpl(realMatrix);
		RealMatrix U = SVD.getU();
		RealMatrix S = SVD.getS();
		RealMatrix VT = SVD.getVT();
		RealMatrix newRealMatrix = U.multiply(S).multiply(VT);

		float pureSVDFullRankRmse = SVDMethod.calSVDRmse(matrix, originalMatrix, newRealMatrix);
		System.out.println("pureSVDFullRank Rmse:\t" + pureSVDFullRankRmse);
	}

	static void pureSVDEachRank(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix, float mu,
			List<Float> buList, List<Float> biList) {
		int row = matrix.size();
		int col = matrix.get(0).size();
		double[][] M = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (matrix.get(i).get(j) != 0f) {
					M[i][j] = matrix.get(i).get(j) * constants;
				} else {
					M[i][j] = (mu + buList.get(i) + biList.get(j)) * constants;
				}
			}
		}

		RealMatrix realMatrix = MatrixUtils.createRealMatrix(M);
		System.out.println(new Date() + ":flag1:" + row +"," + col);
		SingularValueDecomposition SVD = new SingularValueDecompositionImpl(realMatrix);
		System.out.println(new Date() + ":flag2");
		RealMatrix U = SVD.getU();
		RealMatrix S = SVD.getS();
		RealMatrix VT = SVD.getVT();
		System.out.println(new Date() + ":flag3");
		
		outputMatrix(realMatrix, "realMatrix");
		outputMatrix(U, "U");
		outputMatrix(S, "S");
		outputMatrix(VT, "VT");
		
		int numberLambda = 0;
		for (int i = 0; i < S.getRowDimension(); i++) {
			if (S.getEntry(i, i) == 0) {
				numberLambda = i;
				break;
			}
		}
		System.out.println("numberLambda: " + numberLambda);

		float minSVDRmse = 100000.0f;
		int bestNumberLambda = 0;
		RealMatrix SS = MatrixUtils.createRealMatrix(S.getData());
		outputMatrix(SS, "SS");
		for (int i = numberLambda; i >= 0; i -= 100) {
			for(int j  = numberLambda; j >= i; j--) {
				SS.setEntry(j, j, 0.0);
			}
			outputMatrix(SS, "SS_" + String.valueOf(i));
			RealMatrix newRealMatrix = U.multiply(SS).multiply(VT);
			outputMatrix(newRealMatrix, "newRealMatrix_" + String.valueOf(i));
			float rmse = calSVDRmse(matrix, originalMatrix, newRealMatrix);
			System.out.println(new Date() + "[" + i + "]: " + rmse);
			if (rmse < minSVDRmse) {
				minSVDRmse = rmse;
				bestNumberLambda = i;
			}
		}
		System.out.println("pureSVDEachRank Rmse: " + minSVDRmse + "[" + bestNumberLambda + "]");
	}

	static float calSVDRmse(List<ArrayList<Float>> matrix, List<ArrayList<Float>> originalMatrix,
			RealMatrix newRealMatrix) {
		float sum = 0f;
		float valid = 0f;
		for (int i = 0; i < matrix.size(); i++) {
			for (int j = 0; j < matrix.get(0).size(); j++) {
				if (matrix.get(i).get(j) == 0 && originalMatrix.get(i).get(j) != 0) {
					sum += Math.pow((newRealMatrix.getEntry(i, j) / constants - originalMatrix.get(i).get(j)), 2.0);
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
	
	
	static void outputMatrix(RealMatrix  realMatrix, String outputName) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("data/track1/" + outputName));
			for(int i=0; i<realMatrix.getRowDimension(); i++) {
				for(int j=0; j<realMatrix.getColumnDimension(); j++) {
					out.write(realMatrix.getEntry(i, j) + " ");
				}
				out.write("\n");
			}	
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

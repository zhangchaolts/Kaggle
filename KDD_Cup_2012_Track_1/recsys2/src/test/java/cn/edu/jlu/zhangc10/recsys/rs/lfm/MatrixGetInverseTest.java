package cn.edu.jlu.zhangc10.recsys.rs.lfm;

import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;

public class MatrixGetInverseTest {

	public static void main(String[] args) {
		double[][] M = { { 0, 1, 2 }, { 1, 1, 4 }, { 2, -1, 0 } };
		RealMatrix realMatrix = MatrixUtils.createRealMatrix(M);
		RealMatrix inverseMatrix = new LUDecompositionImpl(realMatrix).getSolver().getInverse(); // 求逆矩阵
		System.out.println(inverseMatrix);
	}

}

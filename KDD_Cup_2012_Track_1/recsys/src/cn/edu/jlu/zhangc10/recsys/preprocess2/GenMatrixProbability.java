package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class GenMatrixProbability {

	private static final long timesThreshold = 20;
	private static String inputpath1 = "data/track1/new_matrix";
	private static String outputpath1 = "data/track1/new_matrix_iid";
	private static String outputpath2 = "data/track1/new_matrix_probability";

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
			BufferedWriter out2 = new BufferedWriter(new FileWriter(outputpath2));

			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				String[] terms = line.split("\t");
				String iid = terms[0];
				out1.write(iid + "\n");
				String[] items = terms[1].split(" ");
				for (int i = 0; i < items.length; i++) {
					String[] subTerms = items[i].split(",");
					long numPositive = Long.valueOf(subTerms[0]);
					long numNegative = Long.valueOf(subTerms[1]);
					if (numPositive + numNegative >= timesThreshold) {
						out2.write(String.format("%.4f", (double) numPositive / (double) (numPositive + numNegative))
								+ " ");
					} else {
						out2.write("0.0000 ");
					}
				}
				out2.write("\n");
				ptr++;
				if (ptr % 100 == 0) {
					System.out.println(ptr);
				}
			}
			in1.close();
			out1.close();
			out2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

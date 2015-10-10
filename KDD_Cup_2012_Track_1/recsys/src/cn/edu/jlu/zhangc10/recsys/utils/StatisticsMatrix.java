package cn.edu.jlu.zhangc10.recsys.utils;

import java.io.BufferedReader;
import java.io.FileReader;

public class StatisticsMatrix {

	private static String inputpath1 = "data/track1/new_matrix";

	private static void statistics1() {
		long total = 0;
		long totalup1 = 0;
		long totalup3 = 0;
		long totalup5 = 0;
		long totalup10 = 0;
		long totalup20 = 0;
		long totalup30 = 0;
		long totalup50 = 0;

		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			String line;
			int ptr = 0;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 100 == 0) {
					System.out.println(ptr);
				}
				String[] terms = line.split("\t")[1].split(" ");
				for (int i = 0; i < terms.length; i++) {
					String[] subTerms = terms[i].split(",");
					long numPositive = Long.valueOf(subTerms[0]);
					long numNegative = Long.valueOf(subTerms[1]);
					total++;
					if (numPositive + numNegative >= 1) {
						totalup1++;
					}
					if (numPositive + numNegative >= 3) {
						totalup3++;
					}
					if (numPositive + numNegative >= 5) {
						totalup5++;
					}
					if (numPositive + numNegative >= 10) {
						totalup10++;
					}
					if (numPositive + numNegative >= 20) {
						totalup20++;
					}
					if (numPositive + numNegative >= 30) {
						totalup30++;
					}
					if (numPositive + numNegative >= 50) {
						totalup50++;
					}
				}
			}
			in1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("total:" + total);
		System.out.println("totalup1:" + totalup1);
		System.out.println("totalup3:" + totalup3);
		System.out.println("totalup5:" + totalup5);
		System.out.println("totalup10:" + totalup10);
		System.out.println("totalup20:" + totalup20);
		System.out.println("totalup30:" + totalup30);
		System.out.println("totalup50:" + totalup50);
		/* 
		 * matrix
		 * total:24950464 totalup1:6084652 totalup3:3661991 totalup5:2788221
		 * totalup10:1927278 totalup20:1322587 totalup30:1050851
		 * totalup50:771265
		 */
	}

	public static void main(String[] args) {
		statistics1();
	}

}

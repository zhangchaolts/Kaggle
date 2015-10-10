package cn.edu.jlu.zhangc10.recsys.preprocess2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenTrainAndTest {

	private static String inputpath1 = "data/track1/original/rec_log_train.txt";
	private static String outputpath1 = "data/track1/new_train";
	private static String outputpath2 = "data/track1/new_test";

	public static void main(String[] args) throws Exception {
		BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
		BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(outputpath2));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = "2011-11-01 00:00:00";
		Date date1 = sdf.parse(strDate);

		long ptr = 0;
		String line;
		while ((line = in1.readLine()) != null) {
			String[] terms = line.split("\t");

			Date date2 = new Date(Long.valueOf(terms[3]) * 1000);
			
			if (date2.after(date1)) {
				out2.write(terms[0] + "\t" + terms[1] + "\t" + terms[2] + "\n");
			} else {
				out1.write(terms[0] + "\t" + terms[1] + "\t" + terms[2] + "\n");
			}

			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}
		}
		in1.close();
		out1.close();
		out2.close();
	}
}

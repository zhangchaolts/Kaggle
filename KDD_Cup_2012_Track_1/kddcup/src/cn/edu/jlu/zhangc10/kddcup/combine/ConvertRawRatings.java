package cn.edu.jlu.zhangc10.kddcup.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ConvertRawRatings {

	private static String raw_ratings_path = "data/track1/combine_test_instance_ratings_raw";
	private static String rec_log_test_path = "data/track1/original/rec_log_test.txt";
	private static String ratings_path = "data/track1/combine_test_instance_ratings";
	private static List<String> rawRatingsList = new ArrayList<String>();

	public static void init() throws Exception{
		BufferedReader in = new BufferedReader(new FileReader(raw_ratings_path));
		int ptr = 0;
		String line;
		while ((line = in.readLine()) != null) {
			rawRatingsList.add(line);
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("init() : " + ptr / 1000000 + "00w");
			}
		}
		in.close();
	}

	public static void main(String[] args) throws Exception {
		
		init();
		
		BufferedReader in = new BufferedReader(new FileReader(rec_log_test_path));
		BufferedWriter out = new BufferedWriter(new FileWriter(ratings_path));
		
		int ptr = 0;
		String line ;
		while ((line = in.readLine()) != null) {
			out.write(line +"\t" + rawRatingsList.get(ptr) + "\n");
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("main() : " + ptr / 1000000 + "00w");
			}
		}
		in.close();
		out.close();
	}
}

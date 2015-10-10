package cn.edu.jlu.zhangc10.recsys.submission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Rate1 implements Comparable<Rate1> {
	String name;
	double score;

	public Rate1(String name, double score) {
		this.name = name;
		this.score = score;
	}

	@Override
	public int compareTo(Rate1 r) {
		if (this.score < r.score) {
			return 1;
		} else {
			return -1;
		}
	}
}

public class GenSubmissionPart1 {

	public static void main(String[] args) throws IOException {

		BufferedReader in = new BufferedReader(new FileReader("data/track1/submission_ratings_sort"));

		BufferedWriter out = new BufferedWriter(new FileWriter("data/track1/submission_part_1"));
		out.write("id,clicks\n");

		List<Rate1> list = new ArrayList<Rate1>();
		String user = "";
		String preUser = "";
		int ptr = 0;
		String line;

		while ((line = in.readLine()) != null) {
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println(ptr / 1000000 + "00w");
			}

			String[] terms = line.split("\t");

			if (Long.valueOf(terms[3]) >= 1321891200) {
				continue;
			}

			user = terms[0];

			if ((!user.equals(preUser) && !preUser.equals(""))) {

				String info = "";

				Collections.sort(list);

				for (int i = 0; i < Math.min(3, list.size()); i++) {
					if (i == 0) {
						info += "," + list.get(i).name;
					} else {
						info += " " + list.get(i).name;
					}
				}

				out.write(preUser + info + "\n");

				list.clear();
			}

			list.add(new Rate1(terms[1], Double.valueOf(terms[4])));
			preUser = user;
		}

		String info = "";

		Collections.sort(list);

		for (int i = 0; i < Math.min(3, list.size()); i++) {
			if (i == 0) {
				info += "," + list.get(i).name;
			} else {
				info += " " + list.get(i).name;
			}
		}

		out.write(user + info + "\n");

		in.close();
		out.close();

	}

}

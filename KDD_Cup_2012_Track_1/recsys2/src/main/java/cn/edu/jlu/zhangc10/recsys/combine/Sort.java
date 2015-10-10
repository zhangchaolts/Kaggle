package cn.edu.jlu.zhangc10.recsys.combine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Log implements Comparable<Log> {
	String item0;
	String item1;
	String item2;
	String item3;
	String item4;

	public Log(String item0, String item1, String item2, String item3, String item4) {
		this.item0 = item0;
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.item4 = item4;
	}

	@Override
	public int compareTo(Log o) {
		if (Long.valueOf(item0) < Long.valueOf(o.item0)) {
			return 1;
		} else {
			return -1;
		}
	}

}

public class Sort {

	static List<Log> list = new ArrayList<Log>();

	public static void main(String[] args) throws IOException {

		BufferedReader in = new BufferedReader(new FileReader("data/track1/cb_result_final"));

		BufferedWriter out = new BufferedWriter(new FileWriter("data/track1/cb_result_final_sort2"));

		int ptr = 0;
		String line;
		while ((line = in.readLine()) != null) {
			ptr++;
			if (ptr % 1000000 == 0) {
				System.out.println("read:" + ptr / 1000000 + "00w");
			}
			String[] terms = line.split("\t");
			list.add(new Log(terms[0], terms[1], terms[2], terms[3], terms[4]));
		}
		in.close();

		Collections.sort(list);

		for (int i = 0; i < list.size(); i++) {
			if ((i + 1) % 1000000 == 0) {
				System.out.println("write:" + (i + 1) / 1000000 + "00w");
			}
			Log log = list.get(i);
			out.write(log.item0 + "\t" + log.item1 + "\t" + log.item2 + "\t" + log.item3 + "\t" + log.item4 + "\n");
		}

		out.close();

	}

}

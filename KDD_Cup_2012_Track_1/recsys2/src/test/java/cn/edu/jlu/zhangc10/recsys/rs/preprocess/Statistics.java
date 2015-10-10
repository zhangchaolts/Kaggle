package cn.edu.jlu.zhangc10.recsys.rs.preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

public class Statistics extends TestCase {

	public void testGender() throws Exception {
		String inputpath = "data/track1/original/user_profile.txt";
		BufferedReader in = new BufferedReader(new FileReader(inputpath));

		Map<String, Long> genderMap = new HashMap<String, Long>();

		int ptr = 0;
		String line;
		while ((line = in.readLine()) != null) {
			ptr++;
			if (ptr % 10000 == 0) {
				System.out.println(ptr / 10000 + "w");
			}
			String gender = line.split("\t")[2];
			if (genderMap.containsKey(gender)) {
				long exsit = genderMap.get(gender);
				genderMap.put(gender, exsit + 1);
			} else {
				genderMap.put(gender, (long) 1);
			}

		}
		in.close();

		Iterator<String> itr = genderMap.keySet().iterator();
		while (itr.hasNext()) {
			String gender = itr.next();
			System.out.println(gender + ":" + genderMap.get(gender));
		}

	}
}

package cn.edu.jlu.zhangc10.recsys.rs.preprocess;

import java.util.List;

import junit.framework.TestCase;

public class GetUserFeatureArrayTest extends TestCase {

	public void testGetArray() {
		String userFeaturesArray = GetUserFeatureArray.getArray("8	2	1	2");
		System.out.println("userFeaturesArray:" + userFeaturesArray);
		System.out.println("userFeaturesArray.split(\" \").length:" + userFeaturesArray.split(" ").length);
		String[] terms = userFeaturesArray.split(" ");
		for (int i = 0; i < terms.length; i++) {
			if (terms[i].equals("1")) {
				System.out.println(i);
			}
		}

		List<Integer> featurePtrList = GetUserFeatureArray.getFeaturePtrList("8	2	1	2");
		for (int v : featurePtrList) {
			System.out.println(v);
		}
	}

}

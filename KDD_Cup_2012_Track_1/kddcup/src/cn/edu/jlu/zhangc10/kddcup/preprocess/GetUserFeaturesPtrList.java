package cn.edu.jlu.zhangc10.kddcup.preprocess;

import java.util.ArrayList;
import java.util.List;

public class GetUserFeaturesPtrList {

	public static final int NUM_BIRTH_GROUPS = 15;
	public static final int NUM_SEX_GROUPS = 3;
	public static final int NUM_ACTIVITY_GROUPS = 5;
	public static final int NUM_USER_TAGS_GROUPS = 31;
	
	public static void main(String[] args) {
		List<Integer> featurePtrList = getFeaturesPtrList("8	2	1	2");
		for (int v : featurePtrList) {
			System.out.println(v);
		}
		
		String userFeaturesArray = getArray("8	2	1	2");
		System.out.println("userFeaturesArray.split(\" \").length:" + userFeaturesArray.split(" ").length);
		String[] terms = userFeaturesArray.split(" ");
		for (int i = 0; i < terms.length; i++) {
			if (terms[i].equals("1")) {
				System.out.println(i);
			}
		}
	}

	public static List<Integer> getFeaturesPtrList(String userInfo) {
		String[] terms = userInfo.split("\t");
		int a = Integer.valueOf(terms[0]);
		int b = Integer.valueOf(terms[1]);
		int c = Integer.valueOf(terms[2]);
		int d = Integer.valueOf(terms[3]);
		List<Integer> featurePtrList = new ArrayList<Integer>();
		featurePtrList.add(a);
		featurePtrList.add(15 + b);
		featurePtrList.add(15 + 3 + c);
		featurePtrList.add(15 + 3 + 5 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + a * 3 + b);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + a * 5 + c);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + a * 31 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + b * 5 + c);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + b * 31 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + 3 * 31 + c * 31 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + 3 * 31 + 5 * 31 + a * 3 * 5 + b * 5
				+ c);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + 3 * 31 + 5 * 31 + 15 * 3 * 5 + a * 5
				* 31 + c * 31 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + 3 * 31 + 5 * 31 + 15 * 3 * 5 + 15 * 5
				* 31 + a * 3 * 31 + b * 31 + d);
		featurePtrList.add(15 + 3 + 5 + 31 + 15 * 3 + 15 * 5 + 15 * 31 + 3 * 5 + 3 * 31 + 5 * 31 + 15 * 3 * 5 + 15 * 5
				* 31 + 15 * 3 * 31 + b * 5 * 31 + c * 31 + d);
		return featurePtrList;
	}

	public static String getArray(String userInfo) {
		String[] terms = userInfo.split("\t");
		String birthGroup = terms[0];
		String sexGroup = terms[1];
		String activityGroup = terms[2];
		String userTagsGroup = terms[3];
		String result = generate1DInfo(birthGroup, NUM_BIRTH_GROUPS)
				+ generate1DInfo(sexGroup, NUM_SEX_GROUPS)
				+ generate1DInfo(activityGroup, NUM_ACTIVITY_GROUPS)
				+ generate1DInfo(userTagsGroup, NUM_USER_TAGS_GROUPS)
				+ generate2DInfo(birthGroup, NUM_BIRTH_GROUPS, sexGroup, NUM_SEX_GROUPS)
				+ generate2DInfo(birthGroup, NUM_BIRTH_GROUPS, activityGroup, NUM_ACTIVITY_GROUPS)
				+ generate2DInfo(birthGroup, NUM_BIRTH_GROUPS, userTagsGroup, NUM_USER_TAGS_GROUPS)
				+ generate2DInfo(sexGroup, NUM_SEX_GROUPS, activityGroup, NUM_ACTIVITY_GROUPS)
				+ generate2DInfo(sexGroup, NUM_SEX_GROUPS, userTagsGroup, NUM_USER_TAGS_GROUPS)
				+ generate2DInfo(activityGroup, NUM_ACTIVITY_GROUPS, userTagsGroup, NUM_USER_TAGS_GROUPS)
				+ generate3DInfo(birthGroup, NUM_BIRTH_GROUPS, sexGroup, NUM_SEX_GROUPS, activityGroup,
						NUM_ACTIVITY_GROUPS)
				+ generate3DInfo(birthGroup, NUM_BIRTH_GROUPS, activityGroup, NUM_ACTIVITY_GROUPS, userTagsGroup,
						NUM_USER_TAGS_GROUPS)
				+ generate3DInfo(birthGroup, NUM_BIRTH_GROUPS, sexGroup, NUM_SEX_GROUPS, userTagsGroup,
						NUM_USER_TAGS_GROUPS)
				+ generate3DInfo(sexGroup, NUM_SEX_GROUPS, activityGroup, NUM_ACTIVITY_GROUPS, userTagsGroup,
						NUM_USER_TAGS_GROUPS);
		return result;

	}

	private static String generate1DInfo(String group, int totalGroups) {
		int groupValue = Integer.valueOf(group);
		String result = "";
		for (int i = 0; i < totalGroups; i++) {
			if (i == groupValue) {
				result += "1 ";
			} else {
				result += "0 ";
			}
		}
		return result;
	}

	private static String generate2DInfo(String group1, int totalGroups1, String group2, int totalGroups2) {
		int groupValue1 = Integer.valueOf(group1);
		int groupValue2 = Integer.valueOf(group2);
		String result = "";
		for (int i = 0; i < totalGroups1; i++) {
			for (int j = 0; j < totalGroups2; j++) {
				if (i == groupValue1 && j == groupValue2) {
					result += "1 ";
				} else {
					result += "0 ";
				}
			}
		}
		return result;
	}

	private static String generate3DInfo(String group1, int totalGroups1, String group2, int totalGroups2,
			String group3, int totalGroups3) {
		int groupValue1 = Integer.valueOf(group1);
		int groupValue2 = Integer.valueOf(group2);
		int groupValue3 = Integer.valueOf(group3);
		String result = "";
		for (int i = 0; i < totalGroups1; i++) {
			for (int j = 0; j < totalGroups2; j++) {
				for (int k = 0; k < totalGroups3; k++) {
					if (i == groupValue1 && j == groupValue2 && k == groupValue3) {
						result += "1 ";
					} else {
						result += "0 ";
					}
				}
			}
		}
		return result;
	}
}

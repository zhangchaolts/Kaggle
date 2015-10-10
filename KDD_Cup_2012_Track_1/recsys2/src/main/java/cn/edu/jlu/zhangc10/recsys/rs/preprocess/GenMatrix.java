package cn.edu.jlu.zhangc10.recsys.rs.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Cell {
	int numPositive;
	int numNegative;

	public Cell(int numPositive, int numNegative) {
		this.numPositive = numPositive;
		this.numNegative = numNegative;
	}
}

public class GenMatrix {

	private static String inputpath1 = "data/track1/user_features";
	private static String inputpath2 = "data/track1/train_change";
	private static String outputpath1 = "data/track1/matrix";
	private static Map<String, String> userFeaturesMap = new HashMap<String, String>();
	private static Map<String, List<Cell>> itemInfoMap = new HashMap<String, List<Cell>>();

	public static void main(String[] args) {
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(inputpath1));
			BufferedReader in2 = new BufferedReader(new FileReader(inputpath2));
			BufferedWriter out1 = new BufferedWriter(new FileWriter(outputpath1));

			int ptr = 0;
			String line;
			while ((line = in1.readLine()) != null) {
				ptr++;
				if (ptr % 100000 == 0) {
					System.out.println(ptr);
				}
				String[] terms = line.split("\t");
				userFeaturesMap.put(terms[0], terms[1] + "\t" + terms[2] + "\t" + terms[3] + "\t" + terms[4]);
			}
			in1.close();

			int numNoFeatureUsers = 0;
			ptr = 0;
			while ((line = in2.readLine()) != null) {
				ptr++;
				if (ptr % 100 == 0) {
					System.out.println(new Date() + ":" + ptr);
				}

				String[] terms = line.split("\t");
				String uid = terms[0];
				String itemsInfo = terms[1];

				String userFeatures;
				if (!userFeaturesMap.containsKey(uid)) {
					numNoFeatureUsers++;
					continue;
				} else {
					userFeatures = userFeaturesMap.get(uid);
				}
				String userFeatureArray = GetUserFeatureArray.getArray(userFeatures);

				String[] items = itemsInfo.split(";");
				for (int i = 0; i < items.length; i++) {
					String[] subTerms = items[i].split(":");
					String[] subSubTerms = subTerms[0].split(",");
					String item = subSubTerms[0];
					String label = subSubTerms[1];
					int times = Integer.valueOf(subTerms[1]);

					Cell cell = null;
					if (label.equals("1")) {
						cell = new Cell(times, 0);
					} else {
						cell = new Cell(0, times);
					}

					if (itemInfoMap.containsKey(item)) {
						List<Cell> existCellList = itemInfoMap.get(item);
						String[] features = userFeatureArray.split(" ");
						for (int j = 0; j < features.length; j++) {
							if (features[j].equals("1")) {
								Cell originalCell = existCellList.get(j);
								existCellList.set(j, new Cell(originalCell.numPositive + cell.numPositive,
										originalCell.numNegative + cell.numNegative));
							}
						}
					} else {
						List<Cell> cellList = new ArrayList<Cell>();
						String[] features = userFeatureArray.split(" ");
						for (int j = 0; j < features.length; j++) {
							if (features[j].equals("1")) {
								cellList.add(new Cell(cell.numPositive, cell.numNegative));
							} else {
								cellList.add(new Cell(0, 0));
							}
						}
						itemInfoMap.put(item, cellList);
					}
				}
			}
			in2.close();
			System.out.println("numNoFeatureUsers:" + numNoFeatureUsers);

			Iterator<String> itr = itemInfoMap.keySet().iterator();
			while (itr.hasNext()) {
				String item = itr.next();
				List<Cell> cellList = itemInfoMap.get(item);
				out1.write(item + "\t");
				for (Cell cell : cellList) {
					out1.write(cell.numPositive + "," + cell.numNegative + " ");
				}
				out1.write("\n");
			}
			out1.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
